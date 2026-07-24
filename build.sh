#!/bin/bash

echo "=========================================="
echo "  COSMIC ODYSSEY RPG - Build Script"
echo "=========================================="
echo ""

# Check if Android SDK is installed
if [ -z "$ANDROID_HOME" ] && [ -z "$ANDROID_SDK_ROOT" ]; then
    echo "❌ Erreur: ANDROID_HOME ou ANDROID_SDK_ROOT non défini"
    echo "Installe Android SDK et exporte la variable:"
    echo "  export ANDROID_HOME=/chemin/vers/android-sdk"
    exit 1
fi

SDK_DIR="${ANDROID_HOME:-$ANDROID_SDK_ROOT}"
echo "✅ Android SDK trouvé: $SDK_DIR"

# Check Java
if ! command -v java &> /dev/null; then
    echo "❌ Erreur: Java non installé"
    echo "  apt install openjdk-17-jdk"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
echo "✅ Java: $JAVA_VERSION"

# Download Gradle wrapper if needed
if [ ! -f "gradlew" ]; then
    echo "📥 Téléchargement du Gradle Wrapper..."
    mkdir -p gradle/wrapper
    
    cat > gradle/wrapper/gradle-wrapper.properties << 'GRADLE'
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-8.2-bin.zip
networkTimeout=10000
validateDistributionUrl=true
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
GRADLE

    curl -L -o gradle/wrapper/gradle-wrapper.jar \
        https://raw.githubusercontent.com/gradle/gradle/v8.2.0/gradle/wrapper/gradle-wrapper.jar
    
    cat > gradlew << 'WRAPPER'
#!/bin/sh
exec "$(dirname "$0")/gradle/wrapper/gradle-wrapper.jar" "$@"
WRAPPER
    chmod +x gradlew
fi

echo ""
echo "🔨 Compilation en cours..."
echo ""

./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "  ✅ BUILD RÉUSSI !"
    echo "=========================================="
    echo ""
    echo "APK généré:"
    echo "  app/build/outputs/apk/debug/app-debug.apk"
    echo ""
    echo "Pour installer sur ton S23 Ultra:"
    echo "  adb install app/build/outputs/apk/debug/app-debug.apk"
    echo ""
else
    echo ""
    echo "=========================================="
    echo "  ❌ BUILD ÉCHOUÉ"
    echo "=========================================="
    exit 1
fi
