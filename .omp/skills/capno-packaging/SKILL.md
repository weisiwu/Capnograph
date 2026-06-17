---
name: capno-packaging
description: Packaging, Docker, CI, and release workflow guidance for the CapnoGraph monorepo. Use for scripts/package.sh, Android Docker builder, Gradle/Xcode packaging, artifacts, or release validation.
globs:
  - "scripts/package.sh"
  - "docker/**"
  - "compose.yaml"
  - "README.md"
  - "apps/android/**"
  - "apps/ios/**"
---

# CapnoGraph Packaging Skill

Use this skill for repository-level build, packaging, Docker, and release tasks.

## Entrypoint

Always prefer the root wrapper unless a task explicitly requires a platform-local command:

```bash
scripts/package.sh --target android --variant debug
scripts/package.sh --target android --variant release -- --no-daemon
scripts/package.sh --target ios --variant debug
scripts/package.sh --target ios --variant release --export-options-plist ExportOptions.plist
```

Use `capno_package` for direct command previews/execution.
Use `capno_packflow` for a full packaging workflow with artifact collection and optional Feishu webhook notification.

## Android Packaging

- The wrapper changes into `apps/android` and runs `./gradlew :app:assembleDebug` or `./gradlew :app:assembleRelease`.
- The Android builder image lives under `docker/android-builder`.
- The Docker image is intended to avoid relying on a developer machine Android SDK.
- Release signing is not currently defined in the Android project; production release packaging needs environment-driven signing added before APK/AAB signing.

## iOS Packaging

- The wrapper changes into `apps/ios`.
- Debug builds default to `generic/platform=iOS Simulator`.
- Release builds default to archive for `generic/platform=iOS`.
- Default DerivedData path is `apps/ios/build/DerivedData`.
- Exporting an IPA requires `--export-options-plist`.
- `capno_packflow` can call `capno_feishu_send`-style webhook payloads for post-build delivery to a Feishu机器人.

## Common Pitfalls

- Keep machine-specific JDK, Android SDK, Xcode, provisioning, and signing assets out of the repository.
- Do not bypass `scripts/package.sh` for root-level automation unless you need a precise platform-local task.
- When changing build scripts, update README and `context/docs/build-platform.md` if durable packaging behavior changes.
- For dependency repository changes, verify fresh-cache behavior; CI/Docker often exposes missing plugin or Maven coordinates that local caches hide.
