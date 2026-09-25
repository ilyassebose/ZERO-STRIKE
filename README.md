<div align="center">

# ZERO STRIKE

**FPS 3D tactique multijoueur pour Android**

[![Build](https://img.shields.io/github/actions/workflow/status/ilyassebose/ZERO-STRIKE/build.yml?style=flat-square&label=build)](https://github.com/ilyassebose/ZERO-STRIKE/actions)
[![Release](https://img.shields.io/github/v/release/ilyassebose/ZERO-STRIKE?style=flat-square&label=release)](https://github.com/ilyassebose/ZERO-STRIKE/releases)
[![Downloads](https://img.shields.io/github/downloads/ilyassebose/ZERO-STRIKE/total?style=flat-square&label=downloads)](https://github.com/ilyassebose/ZERO-STRIKE/releases)
[![License](https://img.shields.io/github/license/ilyassebose/ZERO-STRIKE?style=flat-square&label=license)](LICENSE)
[![PEGI](https://img.shields.io/badge/PEGI-16-red?style=flat-square)](https://pegi.info)

Un FPS mobile nerveux, pensé pour le jeu en réseau local. Pas de serveurs distants, pas de latence : tu lances une partie, tes amis rejoignent en WiFi, et ça tire.

[Releases](https://github.com/ilyassebose/ZERO-STRIKE/releases) · [Issues](https://github.com/ilyassebose/ZERO-STRIKE/issues) · [Signaler un bug](https://github.com/ilyassebose/ZERO-STRIKE/issues/new)

</div>

---

## Sommaire

- [À propos](#à-propos)
- [Fonctionnalités](#fonctionnalités)
- [Installation](#installation)
- [Contrôles](#contrôles)
- [Multijoueur](#multijoueur)
- [Stack technique](#stack-technique)
- [Compilation](#compilation)
- [FAQ](#faq)
- [Licence](#licence)

---

## À propos

**ZERO STRIKE** est un FPS 3D tactique pour Android, développé en solo par [ilyassebose](https://github.com/ilyassebose). Il mise sur le rendu réaliste via **Google Filament** et sur le jeu en **réseau local WiFi**, sans dépendre d'un serveur externe.

Le projet est open source sous licence MIT. Les mises à jour sont distribuées directement via GitHub Releases, avec un système d'auto-update intégré à l'application.

---

## Fonctionnalités

| Fonctionnalité | Description |
|---|---|
| **FPS 3D tactique** | Gameplay nerveux, visée précise, maps pensées pour le combat rapproché |
| **Multijoueur WiFi local** | Jusqu'à **8 joueurs** sur le même réseau, sans serveur distant |
| **Serveur intégré** | Un joueur héberge la partie via un serveur WebSocket embarqué |
| **5 bots IA** | Bots autonomes pour compléter les équipes ou s'entraîner seul |
| **Support manette** | Compatible manettes Xbox et PlayStation (Bluetooth / USB) |
| **Contrôles tactiles** | Joystick virtuel et gestes style Minecraft mobile |
| **Éclairage PBR** | Rendu physique réaliste via Google Filament |
| **Pluie temps réel** | Effets météo dynamiques en jeu |
| **Kill feed avec XP** | Suivi des éliminations et progression par points d'expérience |
| **Auto-update** | Mise à jour automatique via l'API GitHub Releases |
| **60 FPS stable** | Optimisé pour une fluidité constante sur mobile |

---

## Installation

1. Rends-toi sur la page [**Releases**](https://github.com/ilyassebose/ZERO-STRIKE/releases).
2. Télécharge le fichier **`.apk`** de la dernière version.
3. Sur ton appareil Android, autorise l'installation depuis **des sources inconnues** :
   - `Paramètres` → `Sécurité` → `Sources inconnues` (ou `Installer des applis inconnues` selon la version d'Android).
4. Ouvre le fichier APK téléchargé et installe-le.
5. Lance **ZERO STRIKE** depuis ton tiroir d'applications.

> **Prérequis :** Android 8.0 (API 26) ou supérieur, GPU compatible Vulkan ou OpenGL ES 3.1.

---

## Contrôles

### Tactile

| Élément | Action |
|---|---|
| **Joystick gauche** | Déplacement du personnage |
| **Glisser à droite** | Regarder / orienter la caméra |
| **Bouton tir** | Tirer |
| **Bouton saut** | Sauter |
| **Bouton recharger** | Recharger l'arme |

### Manette

| Bouton | Action |
|---|---|
| **Stick gauche** | Déplacement |
| **Stick droit** | Visée |
| **RT / R2** | Tirer |
| **A / Croix** | Sauter |
| **X / Carré** | Recharger |

---

## Multijoueur

ZERO STRIKE fonctionne en **réseau local (LAN)** :

1. Un joueur crée une partie : il devient l'**hôte** et lance le serveur WebSocket intégré.
2. Les autres joueurs rejoignent via l'**adresse IP locale** affichée par l'hôte.
3. Jusqu'à **8 joueurs** peuvent se connecter simultanément.
4. Si l'hôte quitte, la partie se termine.

Aucune connexion Internet n'est requise pour jouer en local.

---

## Stack technique

```text
Rendu 3D        : Google Filament
Interface       : HTML / CSS / JS dans une WebView
Packaging       : Capacitor
Réseau          : WebSocket (Java-WebSocket)
Build           : GitHub Actions
Auto-update     : GitHub Releases API
```

---

## Compilation

### Prérequis

- Android Studio (dernière version stable)
- JDK 17
- Android SDK (API 26+)
- Node.js 18+

### Étapes

```bash
# Cloner le dépôt
git clone https://github.com/ilyassebose/ZERO-STRIKE.git
cd ZERO-STRIKE

# Installer les dépendances
npm install

# Synchroniser Capacitor
npx cap sync android

# Compiler l'APK debug
cd android
./gradlew assembleDebug
```

L'APK généré se trouve dans :

```text
android/app/build/outputs/apk/debug/app-debug.apk
```

Pour une build de release signée :

```bash
./gradlew assembleRelease
```

---

## FAQ

<details>
<summary><strong>Le jeu est-il gratuit ?</strong></summary>

Oui. ZERO STRIKE est entièrement gratuit et open source sous licence MIT.

</details>

<details>
<summary><strong>Faut-il une connexion Internet pour jouer ?</strong></summary>

Non. Le multijoueur fonctionne en **WiFi local**. Internet n'est nécessaire que pour télécharger les mises à jour.

</details>

<details>
<summary><strong>Combien de joueurs peuvent rejoindre une partie ?</strong></summary>

Jusqu'à **8 joueurs** simultanément sur le même réseau local.

</details>

<details>
<summary><strong>Puis-je jouer avec une manette ?</strong></summary>

Oui. Les manettes **Xbox** et **PlayStation** sont supportées en Bluetooth ou USB.

</details>

<details>
<summary><strong>Comment sont distribuées les mises à jour ?</strong></summary>

Via l'**API GitHub Releases**. L'application vérifie automatiquement les nouvelles versions et propose de les installer.

</details>

<details>
<summary><strong>Sur quels appareils le jeu tourne-t-il ?</strong></summary>

Android 8.0 (API 26) minimum, avec un GPU compatible Vulkan ou OpenGL ES 3.1. Les appareils récents offrent les meilleures performances (60 FPS stables).

</details>

<details>
<summary><strong>Comment signaler un bug ou proposer une idée ?</strong></summary>

Ouvre une [issue](https://github.com/ilyassebose/ZERO-STRIKE/issues) sur GitHub. Les rapports détaillés (appareil, version Android, étapes de reproduction) sont les bienvenus.

</details>

<details>
<summary><strong>Le jeu est-il adapté aux enfants ?</strong></summary>

ZERO STRIKE est classé **PEGI 16** en raison de la violence représentée dans un contexte de tir.

</details>

---

## Licence

Distribué sous licence **MIT**. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

<div align="center">

**ZERO STRIKE** — Développé par [ilyassebose](https://github.com/ilyassebose)

Si le projet te plaît, mets une ⭐ sur le repo.

[Releases](https://github.com/ilyassebose/ZERO-STRIKE/releases) · [Issues](https://github.com/ilyassebose/ZERO-STRIKE/issues) · [Licence MIT](LICENSE)

</div>
