(function() {
    'use strict';
    var BOT_NAMES = ['Kevin', 'Max', 'Steve', 'Ghost', 'Reaper', 'Viper', 'Falcon', 'Wolf'];
    var BOT_COUNT = 5;

    window.BotManager = {
        bots: [],
        spawnAll: function(scene, spawnPoints) {
            this.bots = [];
            for (var i = 0; i < BOT_COUNT; i++) {
                var bot = this.createBot(i, spawnPoints[i % spawnPoints.length]);
                scene.add(bot.mesh);
                this.bots.push(bot);
            }
            console.log('[BOTS] ' + BOT_COUNT + ' bots spawnés');
        },

        createBot: function(index, pos) {
            var bot = {
                id: index,
                name: 'BOT_' + BOT_NAMES[index % BOT_NAMES.length],
                mesh: this.createMesh(),
                state: 'patrol',
                health: 100,
                lastShot: 0,
                fireRate: 500,
                accuracy: 0.7,
                speed: 0.05,
                patrolTarget: this.randomPoint(pos)
            };
            bot.mesh.position.copy(pos);
            bot.mesh.userData.bot = bot;
            return bot;
        },

        createMesh: function() {
            var g = new THREE.Group();
            var body = new THREE.Mesh(
                new THREE.CapsuleGeometry(0.4, 1.2, 4, 8),
                new THREE.MeshStandardMaterial({ color: 0x1a1a2e, metalness: 0.6, roughness: 0.4 })
            );
            body.position.y = 0.9;
            body.castShadow = true;
            g.add(body);

            var head = new THREE.Mesh(
                new THREE.SphereGeometry(0.25, 16, 16),
                new THREE.MeshStandardMaterial({ color: 0xff006e, emissive: 0xff006e, emissiveIntensity: 0.5 })
            );
            head.position.y = 1.8;
            g.add(head);

            var marker = new THREE.Mesh(
                new THREE.ConeGeometry(0.15, 0.3, 8),
                new THREE.MeshBasicMaterial({ color: 0x00f0ff })
            );
            marker.position.y = 2.4;
            marker.rotation.x = Math.PI;
            g.add(marker);
            return g;
        },

        randomPoint: function(o) {
            return new THREE.Vector3(o.x + (Math.random()-0.5)*20, o.y, o.z + (Math.random()-0.5)*20);
        },

        update: function(scene, playerPos) {
            var now = Date.now();
            for (var i = 0; i < this.bots.length; i++) {
                var bot = this.bots[i];
                var d = bot.mesh.position.distanceTo(playerPos);

                if (d < 15) bot.state = 'attack';
                else if (d < 25) bot.state = 'chase';
                else bot.state = 'patrol';

                if (bot.state === 'patrol') {
                    this.moveTo(bot, bot.patrolTarget);
                    if (bot.mesh.position.distanceTo(bot.patrolTarget) < 2) bot.patrolTarget = this.randomPoint(bot.mesh.position);
                } else if (bot.state === 'chase') {
                    this.moveTo(bot, playerPos);
                    this.faceTo(bot, playerPos);
                } else {
                    this.faceTo(bot, playerPos);
                    if (now - bot.lastShot > bot.fireRate) {
                        bot.lastShot = now;
                        if (Math.random() < bot.accuracy && window.onBotShoot) window.onBotShoot(bot, 5);
                    }
                    this.strafe(bot);
                }
            }
        },

        moveTo: function(bot, t) {
            var dir = new THREE.Vector3().subVectors(t, bot.mesh.position);
            dir.y = 0;
            if (dir.length() > 0.5) {
                dir.normalize();
                bot.mesh.position.addScaledVector(dir, bot.speed);
            }
        },

        faceTo: function(bot, t) {
            var dir = new THREE.Vector3().subVectors(t, bot.mesh.position);
            dir.y = 0;
            if (dir.length() > 0.1) bot.mesh.rotation.y = Math.atan2(dir.x, dir.z);
        },

        strafe: function(bot) {
            if (Math.random() < 0.05) bot.strafeDir = Math.random() < 0.5 ? 1 : -1;
            if (bot.strafeDir) {
                bot.mesh.position.x += Math.cos(bot.mesh.rotation.y + Math.PI/2) * bot.strafeDir * bot.speed * 0.7;
                bot.mesh.position.z += Math.sin(bot.mesh.rotation.y + Math.PI/2) * bot.strafeDir * bot.speed * 0.7;
            }
        }
    };

    console.log('[ZERO STRIKE] Bot Manager prêt');
})();
