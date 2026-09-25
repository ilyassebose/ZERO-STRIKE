package com.ilyasse.zerostrike

import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.FrameLayout
import com.getcapacitor.BridgeActivity

class MainActivity : BridgeActivity() {

    private lateinit var filamentSurface: SurfaceView
    private lateinit var renderer: ZeroStrikeRenderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = findViewById<ViewGroup>(android.R.id.content)

        // SurfaceView pour Filament (rendu 3D natif)
        filamentSurface = SurfaceView(this)
        filamentSurface.holder.setFormat(PixelFormat.TRANSLUCENT)

        // Conteneur qui superpose la SurfaceView et la WebView
        val container = FrameLayout(this)
        container.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // 1. Ajouter la SurfaceView Filament (derrière)
        container.addView(filamentSurface)

        // 2. Récupérer la WebView de Capacitor et la rendre transparente
        val webView = bridge.webView
        (webView.parent as? ViewGroup)?.removeView(webView)
        container.addView(webView)

        webView.setBackgroundColor(Color.argb(1, 0, 0, 0))
        webView.setLayerType(WebView.LAYER_TYPE_HARDWARE, null)

        // Pont JavaScript pour contrôler la pluie depuis l'interface web
        webView.addJavascriptInterface(FilamentJsInterface(), "Android")

        // Remplacer le contenu racine par notre conteneur
        root.removeAllViews()
        root.addView(container)

        // Initialiser le renderer Filament
        renderer = ZeroStrikeRenderer(this, filamentSurface)
        filamentSurface.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) { renderer.initialize() }
            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) { renderer.stop() }
        })
    }

    inner class FilamentJsInterface {
        @JavascriptInterface
        fun enableRain(intensity: Float) {
            runOnUiThread { if (::renderer.isInitialized) renderer.enableRain(intensity) }
        }
        @JavascriptInterface
        fun disableRain() {
            runOnUiThread { if (::renderer.isInitialized) renderer.disableRain() }
        }
        @JavascriptInterface
        fun setRainIntensity(intensity: Float) {
            runOnUiThread { if (::renderer.isInitialized) renderer.setRainIntensity(intensity) }
        }
    }

    override fun onResume() { super.onResume(); if (::renderer.isInitialized) renderer.start() }
    override fun onPause() { super.onPause(); if (::renderer.isInitialized) renderer.stop() }
    override fun onDestroy() { super.onDestroy(); if (::renderer.isInitialized) renderer.destroy() }
}
