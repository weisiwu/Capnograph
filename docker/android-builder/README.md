# CapnoGraph Android Builder Image

This image contains the Android build environment needed by `apps/android`.

- JDK: Eclipse Temurin 17
- Android SDK platform: `android-35`
- Android build-tools: `35.0.0`
- Gradle: project wrapper, currently `8.10.2`
- Android Gradle Plugin: project dependency, currently `8.8.0`
- Kotlin: project dependency, currently `2.0.0`

The image intentionally does not copy project source code. Mount the repository at runtime and let the project Gradle wrapper drive the build.

The Android Gradle settings explicitly map the Hilt plugin id to `com.google.dagger:hilt-android-gradle-plugin` so a fresh Docker/CI cache can resolve the plugin without relying on a developer machine cache.

The repository root also declares this image as the `android-builder` service in `compose.yaml`.

```bash
docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --no-daemon
```

```bash
docker run --rm \
  -v "$PWD:/workspace" \
  -v capnograph-gradle-cache:/home/builder/.gradle \
  -w /workspace \
  wei123098/capnograph-android-builder:android-35-agp-8.8.0 \
  'scripts/package.sh --target android --variant debug -- --no-daemon'
```

Release builds use the same entrypoint:

```bash
docker run --rm \
  -v "$PWD:/workspace" \
  -v capnograph-gradle-cache:/home/builder/.gradle \
  -w /workspace \
  wei123098/capnograph-android-builder:android-35-agp-8.8.0 \
  'scripts/package.sh --target android --variant release -- --no-daemon'
```

The current Android project does not define a release signing config. Add environment-driven signing in Gradle before using this image for production APK/AAB signing.
