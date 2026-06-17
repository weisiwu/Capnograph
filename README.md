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
├── .omp/               # Oh My Pi project skills, tools, and prompt additions
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

Android packaging can also run through the pinned Docker builder image declared in `compose.yaml`.

```bash
docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --no-daemon
docker compose run --rm android-builder scripts/package.sh --target android --variant release -- --no-daemon
```

`capno_packflow` is the OMP entrypoint for automated packaging:

```text
target=android
variant=release
capno_packflow params: {"target":"android","variant":"release","run":true,"useDocker":true,"notifyOnSuccess":true}
```

It returns build output plus collected APK/AAB artifacts and can optionally push a text notification to a Feishu robot webhook.

For local OMP → Feishu bot linkage, expose:

- Skill name: `capno-packflow`
- Tool name: `capno_packflow`
- Tool name: `capno_feishu_send` (optional follow-up)

Recommended robot call:

1. Run package + collect artifacts:
   - `target=android`
   - `variant=release`
   - `run=true`
   - `useDocker=true`
   - `notifyOnSuccess=true`
   - `notifyWebhookUrl` from env `FEISHU_WEBHOOK_URL`
2. If you also want a custom follow-up message, call `capno_feishu_send` with:
   - `title`: build tag
   - `message`: extra description
   - `artifactPaths`: returned `artifacts[].relativePath` list

Example OMP tool-call payload in your bot backend:

```json
{
  "tool": "capno_packflow",
  "parameters": {
    "target": "android",
    "variant": "release",
    "run": true,
    "useDocker": true,
    "extraArgs": ["--no-daemon"],
    "notifyOnSuccess": true,
    "notifyWebhookUrl": "https://open.feishu.cn/open-apis/bot/v2/hook/..."
  }
}
```

On success, the tool returns `details.artifacts`:

```json
{"relativePath":"apps/android/app/build/outputs/apk/release/app-release.apk","size":12345678}
```

If your bot needs a separate text message layer, call:

```json
{
  "tool": "capno_feishu_send",
  "parameters": {
    "title": "CapnoGraph Android 打包完成",
    "message": "release 打包完成，请查看产物：",
    "artifactPaths": ["apps/android/app/build/outputs/apk/release/app-release.apk"],
    "webhookUrl": "https://open.feishu.cn/open-apis/bot/v2/hook/..."
  }
}
```

## Oh My Pi

Project-local Oh My Pi configuration lives under `.omp/`.

- Skills: `capno-context`, `capno-android`, `capno-ios-parity`, `capno-packaging`, `capno-packflow`.
- Tools: `capno_context_lookup` for entity/context lookup; `capno_package` for packaging command previews/execution; `capno_packflow` for build workflow + artifact collection + Feishu webhook notification; `capno_feishu_send` for manual Feishu message sending.
- `.omp/config.yml` also exposes the repository-level `skills/context-aware-dev` skill through `skills.customDirectories`.

Start `omp` from the repository root, or use `omp --cwd ~/Desktop/work/WLD/Capnograph`.

## Versions And Dependencies

Android:

- Gradle wrapper: `apps/android/gradle/wrapper/gradle-wrapper.properties`.
- Docker builder image: `wei123098/capnograph-android-builder:android-35-agp-8.8.0`.
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
