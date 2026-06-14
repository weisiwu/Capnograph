# CapnoGraph Monorepo

This branch consolidates the platform-specific CapnoGraph codebases into a single repository layout.

- Android source: `apps/android`, migrated from `android_v1`.
- iOS source: `apps/ios`, imported from `main`.
- Shared project context: `context` and `skills` remain at the repository root.
- Packaging entrypoint: `scripts/package.sh`.

## Layout

```text
.
├── apps/
│   ├── android/        # Android Gradle project
│   └── ios/            # iOS Xcode project
├── context/            # AI-readable project context
├── scripts/
│   └── package.sh      # Target-based packaging wrapper
└── skills/             # AI workflow instructions
```

## Packaging

Use `--target` to select the platform and `--variant` to select the build type.

```bash
scripts/package.sh --target android --variant debug
scripts/package.sh --target android --variant release -- --no-daemon

scripts/package.sh --target ios --variant debug
scripts/package.sh --target ios --variant release --export-options-plist ExportOptions.plist
```

Android builds run from `apps/android` and currently package the `:app` module.
iOS builds run from `apps/ios` with the `CapnoGraph` scheme. By default, Xcode DerivedData is written to `apps/ios/build/DerivedData` so root-level packaging does not depend on a user-specific DerivedData location.

## Versions And Dependencies

Android:

- Gradle wrapper: `apps/android/gradle/wrapper/gradle-wrapper.properties`.
- Android Gradle Plugin: `8.8.0`.
- Kotlin: `2.0.0`.
- App SDKs: `compileSdk 35`, `minSdk 30`, `targetSdk 35`.
- App version: `versionCode 3`, `versionName 1.2`.
- Java bytecode target: `11`.
- Local JDK path must stay outside the repository. Configure `JAVA_HOME`, Android Studio Gradle JDK, or user-level `~/.gradle/gradle.properties`.

iOS:

- Xcode project: `apps/ios/CapnoGraph.xcodeproj`.
- Scheme: `CapnoGraph`.
- Bundle identifier: `WLD.CapnoEasy`.
- App version: `MARKETING_VERSION 1.0.0`, `CURRENT_PROJECT_VERSION 1`.
- Deployment target: iOS `17.0`.
- Signing uses team `WMK2N92G85` for device/archive builds. Local signing assets remain machine-specific unless intentionally committed.

## Development Notes

Keep platform-local tooling inside each app directory. Repository-level automation should call into `apps/android` or `apps/ios` through explicit parameters so later platform branches can be added without changing the root workflow.
