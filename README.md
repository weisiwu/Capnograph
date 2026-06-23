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

## Packflow

The local Packflow backend at `http://localhost:3001` has CapnoGraph registered as project `CapnoGraph`.

| Config | Workdir | Build command | Artifact path |
| --- | --- | --- | --- |
| Android Release APK | `.` | `docker compose run --rm android-builder scripts/package.sh --target android --variant release -- --no-daemon` | `apps/android/app/build/outputs/apk/release/*.apk` |
| Android Debug APK | `.` | `docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --no-daemon` | `apps/android/app/build/outputs/apk/debug/*.apk` |
| iOS Release Archive | `.` | `scripts/package.sh --target ios --variant release` | `apps/ios/build/*.xcarchive/**` |

Use branch `monorepo_v1` for the current monorepo branch when starting a build from Packflow UI or agent tools. Packflow's UI/agent defaults may use `main`, so pass the branch explicitly until the project default branch is changed.

Current Android Packflow status:

- `Android Debug APK` is the fast APK path. With the Packflow workspace and Gradle cache warm, build `6bb1f453-02ab-4b2f-8599-0c6565924701` completed in `29.043s` and collected `apps/android/app/build/outputs/apk/debug/app-debug.apk`. New Android APK outputs are renamed after Gradle finishes to `app-<variant>-v<version>-<yyyyMMdd-HHmmss>.apk`, for example `app-debug-v1.2-20260624-153012.apk`.
- Large APKs should not be uploaded to Feishu as file messages. The current debug APK is about `105690613` bytes, above Feishu IM file upload limits, so `capno_packflow_agent` includes a public Packflow artifact download link (`/api/public/artifacts/<artifactId>/download?token=<token>`) in the Feishu text notification instead.
- Build `df3a3a14-61db-48ba-811e-c87383e3566a` completed in `26.362s`, collected artifact `59d86d7d-a5c5-44b6-8a35-92e9991e80d3`, and was manually notified to Feishu after a local monitoring harness issue. New Feishu notifications use public token download links.
- `Android Release APK` is not yet reliable. It reached `:app:minifyReleaseWithR8` and then failed with `Gradle build daemon disappeared unexpectedly` after `1011.402s`.
- Keep the debug config's install command empty; running `docker compose version` before every build pushed a warm debug run to `60.412s`.

Example agent payload:

```json
{
  "projectName": "CapnoGraph",
  "configName": "Android Debug APK",
  "branch": "monorepo_v1"
}
```

`capno_packflow_agent` is the recommended AI-callable OMP entrypoint for Packflow-backed APK builds. It calls Packflow's local agent API, waits on the Packflow SQLite status table, collects Packflow artifacts, and can notify the Feishu robot `CapnoGraph OMP Bot`.

```json
{
  "tool": "capno_packflow_agent",
  "parameters": {
    "projectName": "CapnoGraph",
    "configName": "Android Debug APK",
    "branch": "monorepo_v1",
    "waitForCompletion": true,
    "timeoutSeconds": 180,
    "notifyFeishu": true,
    "includeArtifactDownloadLinks": true,
    "packflowPublicBaseUrl": "https://packflow.baoganai.com"
  }
}
```

Webhook configuration is environment-driven. Prefer `CAPNOGRAPH_OMP_BOT_WEBHOOK_URL`; `FEISHU_WEBHOOK_URL` and `FEISHU_WEBHOOK` are also supported. If the robot uses signed webhook mode, set `CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET` or `FEISHU_WEBHOOK_SECRET`. Do not commit webhook URLs or secrets.
Set `CAPNOGRAPH_PACKFLOW_PUBLIC_BASE_URL` or `PACKFLOW_PUBLIC_BASE_URL` to a public Packflow origin before sending Feishu notifications. On this machine the tool can also discover `https://packflow.baoganai.com` from `~/.cloudflared/packflow-baoganai.yml`. Large artifacts require a public base URL; the tool fails instead of sending a `localhost` download link. If Packflow sets `PACKFLOW_PUBLIC_DOWNLOAD_SECRET`, pass the same value as `CAPNOGRAPH_PACKFLOW_PUBLIC_DOWNLOAD_SECRET` or `PACKFLOW_PUBLIC_DOWNLOAD_SECRET`; otherwise the public download token falls back to the artifact SHA-256.

`capno_packflow` remains available as the direct local OMP packaging runner:

```text
target=android
variant=debug
capno_packflow params: {"target":"android","variant":"debug","run":true,"useDocker":true,"notifyOnSuccess":true}
```

It returns build output plus collected APK/AAB artifacts and can optionally push a text notification to a Feishu robot webhook.

For local OMP → Feishu bot linkage, expose:

- Skill name: `capno-packflow`
- Tool name: `capno_packflow_bot_command` (raw Feishu text command router)
- Tool name: `capno_packflow_agent` (recommended Packflow backend path)
- Tool name: `capno_packflow_query` (Packflow config/history/detail queries)
- Tool name: `capno_packflow` (direct local runner)
- Tool name: `capno_feishu_send` (optional follow-up)

Recommended robot call:

1. Route raw Feishu user text through `capno_packflow_bot_command`.
2. For build commands, the router maps to `capno_packflow_agent`:
   - `projectName=CapnoGraph`
   - `configName=Android Debug APK`
   - `branch=monorepo_v1`
   - `waitForCompletion=true`
   - `notifyFeishu=true`
3. For query commands, the router maps to `capno_packflow_query`.

Robot command phrases:

| Feishu text | Routed action |
| --- | --- |
| `打包`, `打 APK`, `打 Android 包`, `capno_packflow_agent` | Start `Android Debug APK` Packflow build |
| `打包状态`, `最新打包`, `packflow latest` | Show latest CapnoGraph Packflow build |
| `打包历史`, `最近 10 条打包`, `packflow history` | Show Packflow build history |
| `打包详情 <buildId>`, `packflow build <buildId>` | Show one build's status, artifacts, sha256, and storage path |
| `打包配置`, `packflow info` | Show CapnoGraph Packflow configs |
| `打包帮助`, `packflow help` | Show supported robot commands |
| `打 release 包` | Blocked with a warning because release still fails in R8/minify |

Example raw-text router call in your bot backend:

```json
{
  "tool": "capno_packflow_bot_command",
  "parameters": {
    "text": "打包状态"
  }
}
```

Example build phrase:

```json
{
  "tool": "capno_packflow_bot_command",
  "parameters": {
    "text": "打包",
    "notifyFeishu": true
  }
}
```

You can also call `capno_packflow_agent` directly when your backend already knows it wants to build:

```json
{
  "tool": "capno_packflow_agent",
  "parameters": {
    "projectName": "CapnoGraph",
    "configName": "Android Debug APK",
    "branch": "monorepo_v1",
    "waitForCompletion": true,
    "notifyFeishu": true,
    "notifyMessage": "CapnoGraph OMP Bot：Android debug APK 打包完成。"
  }
}
```

If you also want a custom follow-up message, call `capno_feishu_send` with:

- `title`: build tag
- `message`: extra description
- `artifactPaths`: returned `artifacts[].file_name` or local artifact paths

On success, `capno_packflow_agent` returns `details.build`, `details.artifacts`, and `details.notification`:

```json
{"file_name":"apps/android/app/build/outputs/apk/debug/app-debug-v1.2-20260624-153012.apk","size_bytes":105690613}
```

If your bot needs a separate text message layer, call:

```json
{
  "tool": "capno_feishu_send",
  "parameters": {
    "title": "CapnoGraph Android 打包完成",
    "message": "debug APK 打包完成，请查看 Packflow 产物：",
    "artifactPaths": ["apps/android/app/build/outputs/apk/debug/app-debug-v1.2-20260624-153012.apk"]
  }
}
```

## Oh My Pi

Project-local Oh My Pi configuration lives under `.omp/`.

- Skills: `capno-context`, `capno-android`, `capno-ios-parity`, `capno-packaging`, `capno-packflow`.
- Tools: `capno_context_lookup` for entity/context lookup; `capno_package` for packaging command previews/execution; `capno_packflow_bot_command` for raw Feishu text routing; `capno_packflow_agent` for Packflow-backed AI builds + artifact collection + CapnoGraph OMP Bot notification; `capno_packflow_query` for Packflow config/history/detail queries; `capno_packflow` for direct local build workflow + artifact collection + Feishu webhook notification; `capno_feishu_send` for manual Feishu message sending.
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
