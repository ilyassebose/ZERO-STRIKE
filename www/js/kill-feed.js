(function() {
    'use strict';
    var feed = document.createElement('div');
    feed.className = 'kill-feed';
    document.body.appendChild(feed);

    var ICONS = {
        'HEADSHOT': '🎯', 'KILL': '💀', 'DOUBLE KILL': '⚡',
        'TRIPLE KILL': '🔥', 'LAST SHOT': '🎖️', 'VEHICLE KILL': '🚗',
        'ROCKET KILL': '🚀', 'GRENADE KILL': '💣', 'NO SCOPE': '🔭'
    };
    var XP = {
        'HEADSHOT': 15, 'KILL': 10, 'DOUBLE KILL': 25, 'TRIPLE KILL': 50,
        'LAST SHOT': 10, 'VEHICLE KILL': 20, 'ROCKET KILL': 15,
        'GRENADE KILL': 10, 'NO SCOPE': 20
    };

    window.showKillFeed = function(type, victimName) {
        type = (type || 'KILL').toUpperCase();
        var item = document.createElement('div');
        item.className = 'kill-feed-item';
        if (type === 'HEADSHOT') item.classList.add('headshot');

        var text = type;
        if (victimName) text += ' — ' + victimName;

        item.innerHTML = 
            '<span class="kill-feed-icon">' + (ICONS[type] || '💀') + '</span>' +
            '<span>' + text + '</span>' +
            '<span class="kill-feed-xp">+' + (XP[type] || 10) + ' XP</span>';

        feed.appendChild(item);
        while (feed.children.length > 5) feed.removeChild(feed.firstChild);

        setTimeout(function() {
            item.style.animation = 'killFadeOut 0.5s forwards';
            setTimeout(function() {
                if (item.parentNode) item.parentNode.removeChild(item);
            }, 500);
        }, 3000);
    };

    console.log('[ZERO STRIKE] Kill Feed prêt');
})();
