(function() {
    'use strict';
    const isNative = typeof window.Android !== 'undefined';

    window.Filament = {
        isNative: isNative,
        enableRain: function(i) { if (window.Android?.enableRain) window.Android.enableRain(i || 1.0); },
        disableRain: function() { if (window.Android?.disableRain) window.Android.disableRain(); },
        setRainIntensity: function(i) { if (window.Android?.setRainIntensity) window.Android.setRainIntensity(i); }
    };

    // ─── MANETTE : réception des événements du natif ───
    // Mapping standard Android KeyEvent → touches de jeu
    var KEY_MAP = {
        96: 'A', 97: 'B', 99: 'X', 100: 'Y',       // Boutons face
        102: 'LB', 103: 'RB', 104: 'LT', 105: 'RT', // Gâchettes
        108: 'START', 109: 'SELECT',                // Menu
        19: 'UP', 20: 'DOWN', 21: 'LEFT', 22: 'RIGHT' // D-pad
    };

    window.onGamepadButton = function(keyCode, pressed) {
        var btn = KEY_MAP[keyCode];
        if (!btn) return;
        // Simuler les touches clavier pour le jeu web
        var key = btn === 'A' ? ' ' : btn === 'X' ? 'f' : btn === 'Y' ? 'r' : btn === 'B' ? 'b' : null;
        if (key) {
            document.dispatchEvent(new KeyboardEvent(pressed ? 'keydown' : 'keyup', { key: key }));
        }
        console.log('[GAMEPAD]', btn, pressed ? 'pressé' : 'relâché');
    };

    window.onGamepadStick = function(lx, ly, rx, ry, lt, rt) {
        // Joystick gauche → mouvement WASD
        var deadzone = 0.2;
        if (Math.abs(lx) > deadzone || Math.abs(ly) > deadzone) {
            var key = Math.abs(lx) > Math.abs(ly)
                ? (lx > 0 ? 'd' : 'a')
                : (ly > 0 ? 's' : 'w');
            document.dispatchEvent(new KeyboardEvent('keydown', { key: key }));
            // Relâcher les autres
            ['w','a','s','d'].forEach(function(k) {
                if (k !== key) document.dispatchEvent(new KeyboardEvent('keyup', { key: k }));
            });
        } else {
            ['w','a','s','d'].forEach(function(k) {
                document.dispatchEvent(new KeyboardEvent('keyup', { key: k }));
            });
        }

        // Joystick droit → rotation caméra
        if (Math.abs(rx) > deadzone) {
            // Simuler le mouvement de souris pour tourner
            var mouseEvent = new MouseEvent('mousemove', {
                movementX: rx * 15, movementY: 0
            });
            document.dispatchEvent(mouseEvent);
        }
        if (Math.abs(ry) > deadzone) {
            var mouseEvent = new MouseEvent('mousemove', {
                movementX: 0, movementY: ry * 15
            });
            document.dispatchEvent(mouseEvent);
        }
    };

    if (isNative) {
        setTimeout(function() { window.Filament.enableRain(1.0); }, 2000);
        console.log('[ZERO STRIKE] Mode natif — Filament + Manette actifs');
    } else {
        console.log('[ZERO STRIKE] Mode web');
    }
})();
