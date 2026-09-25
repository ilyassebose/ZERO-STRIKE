package com.ilyasse.zerostrike

import android.content.Context
import android.view.Choreographer
import android.view.Surface
import android.view.SurfaceView
import com.google.android.filament.*
import com.google.android.filament.android.DisplayHelper
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ZeroStrikeRenderer(private val context: Context, private val surfaceView: SurfaceView)
    : Choreographer.FrameCallback {

    private lateinit var engine: Engine
    private lateinit var renderer: Renderer
    private lateinit var scene: Scene
    private lateinit var view: View
    private lateinit var camera: Camera
    private lateinit var materialProvider: MaterialProvider
    private lateinit var assetLoader: AssetLoader
    private lateinit var entityManager: EntityManager
    private lateinit var transformManager: TransformManager
    private lateinit var lightManager: LightManager
    private lateinit var renderableManager: RenderableManager
    private lateinit var uiHelper: UiHelper
    private lateinit var displayHelper: DisplayHelper

    private var choreographer: Choreographer? = null
    private var isRunning = false
    private var cameraEntity: Int = 0
    private var sunEntity: Int = 0
    private var rainParticles = mutableListOf<Int>()
    private val rainCount = 1500
    private val rainArea = 50f
    private val rainHeight = 30f
    private val rainSpeed = 0.8f
    private var rainEnabled = true
    private var rainIntensity = 1f

    fun initialize() {
        engine = Engine.create()
        renderer = engine.createRenderer()
        scene = engine.createScene()
        view = engine.createView()
        view.scene = scene
        view.isPostProcessingEnabled = true

        camera = engine.createCamera(engine.createEntity())
        camera.setExposure(16f, 1f / 125f, 100f)
        cameraEntity = camera.entity

        entityManager = EntityManager.get()
        transformManager = engine.transformManager
        lightManager = engine.lightManager
        renderableManager = engine.renderableManager

        uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
        uiHelper.renderCallback = SurfaceCallback()
        uiHelper.attachTo(surfaceView)
        displayHelper = DisplayHelper(context)

        materialProvider = MaterialProvider(engine)
        assetLoader = AssetLoader(engine, materialProvider, entityManager)

        transformManager.setTransform(cameraEntity, floatArrayOf(
            1f,0f,0f,0f, 0f,1f,0f,0f, 0f,0f,1f,0f, 0f,1.6f,5f,1f))

        createSun()
        createRainSystem()
        start()
    }

    private fun createSun() {
        sunEntity = entityManager.create()
        val b = LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1f, 0.96f, 0.88f).intensity(50000f)
            .direction(0.3f, -0.8f, -0.5f).castShadows(true)
        lightManager.addEntity(sunEntity, b.build(engine))
    }

    private fun createRainSystem() {
        for (i in 0 until rainCount) {
            val entity = entityManager.create()
            val vb = createRainDropGeometry()
            val mat = materialProvider.createMaterialInstance(
                MaterialProvider.MaterialKey(
                    MaterialProvider.MaterialDomain.SURFACE,
                    MaterialProvider.Shading.UNLIT,
                    MaterialProvider.Blending.TRANSPARENT,
                    MaterialProvider.VertexDomain.DEVICE))
            mat.setParameter("baseColor", 0.6f, 0.7f, 1f, 0.3f)

            val builder = RenderableManager.Builder(1)
                .boundingBox(floatArrayOf(-0.01f,-0.3f,-0.01f), floatArrayOf(0.01f,0.3f,0.01f))
                .material(0, mat)
                .geometry(0, RenderableManager.PrimitiveType.TRIANGLES, vb)
                .culling(false).receiveShadows(false).castShadows(false)
            renderableManager.addEntity(entity, builder.build(engine))

            val x = (Math.random() - 0.5).toFloat() * rainArea
            val y = Math.random().toFloat() * rainHeight
            val z = (Math.random() - 0.5).toFloat() * rainArea
            transformManager.setTransform(entity, floatArrayOf(
                1f,0f,0f,0f, 0f,1f,0f,0f, 0f,0f,1f,0f, x,y,z,1f))
            scene.addEntity(entity)
            rainParticles.add(entity)
        }
    }

    private fun createRainDropGeometry(): VertexBuffer {
        val v = floatArrayOf(
            -0.005f,-0.15f,0f, 0f,0f,1f,  0.005f,-0.15f,0f, 0f,0f,1f,
             0.005f, 0.15f,0f, 0f,0f,1f, -0.005f,-0.15f,0f, 0f,0f,1f,
             0.005f, 0.15f,0f, 0f,0f,1f, -0.005f, 0.15f,0f, 0f,0f,1f)
        val vb = VertexBuffer.Builder().bufferCount(1).vertexCount(6)
            .attribute(VertexBuffer.VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 24)
            .attribute(VertexBuffer.VertexAttribute.NORMAL, 0, VertexBuffer.AttributeType.FLOAT3, 12, 24)
            .build(engine)
        vb.setBufferAt(engine, 0, ByteBuffer.allocateDirect(v.size*4)
            .order(ByteOrder.nativeOrder()).asFloatBuffer().put(v))
        return vb
    }

    private fun updateRain() {
        if (!rainEnabled) return
        val speed = rainSpeed * rainIntensity
        for (entity in rainParticles) {
            val t = FloatArray(16)
            transformManager.getTransform(entity, t)
            var y = t[13] - speed
            if (y < 0f) {
                y = rainHeight
                t[12] = (Math.random() - 0.5).toFloat() * rainArea
                t[14] = (Math.random() - 0.5).toFloat() * rainArea
            }
            t[13] = y
            transformManager.setTransform(entity, t)
        }
    }

    fun enableRain(i: Float) { rainEnabled = true; rainIntensity = i }
    fun disableRain() { rainEnabled = false }
    fun setRainIntensity(i: Float) { rainIntensity = i.coerceIn(0f, 2f) }

    fun start() { if (!isRunning) { isRunning = true
        choreographer = Choreographer.getInstance()
        choreographer?.postFrameCallback(this) } }

    fun stop() { isRunning = false; choreographer?.removeFrameCallback(this) }

    fun destroy() {
        stop(); uiHelper.detach()
        assetLoader.destroy(); materialProvider.destroy()
        engine.destroyRenderer(renderer); engine.destroyScene(scene)
        engine.destroyView(view); engine.destroyCamera(camera); engine.destroy()
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (!isRunning) return
        updateRain()
        if (uiHelper.isReadyToRender) {
            renderer.beginFrame(view.swapChain, frameTimeNanos)
            renderer.render(view); renderer.endFrame()
        }
        choreographer?.postFrameCallback(this)
    }

    private inner class SurfaceCallback : UiHelper.RendererCallback {
        override fun onNativeWindowChanged(surface: Surface) {
            if (::engine.isInitialized) {
                renderer.setDisplayInfo(displayHelper)
                displayHelper.attach(renderer, surfaceView.display)
            }
        }
        override fun onDetachedFromSurface() { if (::engine.isInitialized) displayHelper.detach() }
        override fun onResized(width: Int, height: Int) {
            if (::engine.isInitialized) {
                view.viewport = Viewport(0, 0, width, height)
                camera.setProjection(Camera.Projection.PERSPECTIVE, 45.0,
                    width.toDouble()/height.toDouble(), 0.1, 1000.0, Camera.Fov.VERTICAL)
            }
        }
    }
}
