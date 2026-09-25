(function() {
    'use strict';

    window.SpatialAudio = {
        listener: null,
        init: function(camera) {
            if (!THREE.AudioContext) return;
            this.listener = new THREE.AudioListener();
            camera.add(this.listener);
            console.log('[AUDIO] Listener spatialisé attaché à la caméra');
        },

        playAt: function(url, position, refDistance, volume) {
            if (!this.listener) return null;
            var sound = new THREE.PositionalAudio(this.listener);
            var loader = new THREE.AudioLoader();
            loader.load(url, function(buffer) {
                sound.setBuffer(buffer);
                sound.setRefDistance(refDistance || 10);
                sound.setVolume(volume || 1);
                sound.play();
            });
            if (position) {
                var source = new THREE.Object3D();
                source.position.copy(position);
                source.add(sound);
                scene.add(source);
                setTimeout(function() { scene.remove(source); }, 3000);
            }
            return sound;
        }
    };

    console.log('[ZERO STRIKE] Spatial Audio prêt');
})();
