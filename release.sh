#!/bin/bash
VERSION=$1
[ -z "$VERSION" ] && { echo "Usage: ./release.sh 1.0.2"; exit 1; }
echo "🚀 Release v$VERSION"
sed -i "s/private val currentVersion = \"[^\"]*\"/private val currentVersion = \"$VERSION\"/" android-native/com/ilyasse/zerostrike/MainActivity.kt 2>/dev/null || true
sed -i "s/var currentVersion = \"[^\"]*\"/var currentVersion = \"$VERSION\"/" www/filament-game.html 2>/dev/null || true
cp www/filament-game.html www/index.html 2>/dev/null || true
git add .
git commit -m "🎉 Release v$VERSION" || echo "Rien à commit"
git tag -a "v$VERSION" -m "Release $VERSION"
git push origin main
git push origin "v$VERSION"
echo "✅ Release v$VERSION lancée"
