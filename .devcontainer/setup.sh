#!/usr/bin/env bash
set -e

echo "🔧 Configurando Android SDK 34..."

ANDROID_HOME="$HOME/android-sdk"
mkdir -p "$ANDROID_HOME/cmdline-tools"
cd "$ANDROID_HOME/cmdline-tools"

if [ ! -d "latest" ]; then
  curl -sS -o cmdline-tools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
  unzip -q cmdline-tools.zip
  mv cmdline-tools latest
  rm cmdline-tools.zip
fi

export ANDROID_HOME
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools"

yes | sdkmanager --licenses > /dev/null 2>&1 || true
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"

PROJECT_ROOT="$(pwd)"
if [ -f "$PROJECT_ROOT/build.gradle.kts" ]; then
  echo "sdk.dir=$ANDROID_HOME" > "$PROJECT_ROOT/local.properties"
  chmod +x "$PROJECT_ROOT/gradlew" 2>/dev/null || true
  echo "✅ local.properties criado em $PROJECT_ROOT"
fi

echo "✅ Setup completo! Rode: ./gradlew assembleDebug"
