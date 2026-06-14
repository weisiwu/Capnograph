---
name: capno-android
description: Android development guidance for the CapnoGraph/CapnoEasy app. Use for Kotlin, Jetpack Compose, Hilt, Room, BLE, CO2 waveform, PDF export, thermal printing, permissions, Gradle, or Android build/test tasks.
globs:
  - "apps/android/**"
  - "context/docs/app-ui-workflows/**"
  - "context/docs/core-data-protocol/**"
  - "context/docs/platform-resources/**"
---

# CapnoGraph Android Skill

Use this skill for Android work in `apps/android`.

## Project Facts

- Gradle root: `apps/android`.
- Main app module: `:app`, namespace/application id `com.wldmedical.capnoeasy`.
- Thermal printer module: `:hotmeltprint`, namespace `com.wldmedical.hotmeltprint`.
- Gradle wrapper: `apps/android/gradlew`, Gradle 8.10.2.
- Android Gradle Plugin: 8.8.0.
- Kotlin: 2.0.0.
- App SDKs: compile/target 35, min 30.
- Printer module min SDK: 24.
- Java/Kotlin target: 11.

## Architecture Anchors

- App bootstrap: `CapnoEasyApplication`, `BaseActivity`.
- Page layer: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/`.
- Reusable Compose components: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/`.
- Shared state: `AppStateModel`.
- Service/kits layer: `BlueToothKit`, `LocalStorageKit`, `PDFKit`, `AlertAudioKit`, `DatabaseBackupHelperKit`.
- Thermal print wrapper: `apps/android/hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`.

## High-Risk Areas

- BLE connection, GATT service/characteristic handling, anti-hijack handshake, and command checksum logic need source verification and device-aware review.
- CO2 waveform ingestion must preserve timestamp semantics and recording chunk behavior.
- Room schema or entity changes need migration and schema updates under `apps/android/app/schemas`.
- PDF report generation must preserve Chinese font support, page sectioning, watermark settings, and long-record 15-second waveform sections.
- Thermal printing changes need bitmap/rotation and real printer assumptions checked.
- Permission/Manifest changes affect Bluetooth, storage, location, privacy, and store review risk.

## Validation

Prefer the smallest task that covers the change:

```bash
cd apps/android
./gradlew :app:assembleDebug
./gradlew :hotmeltprint:assembleDebug
./gradlew :app:kaptDebugKotlin
./gradlew :app:testDebugUnitTest :hotmeltprint:testDebugUnitTest
```

From the repository root, packaging uses:

```bash
scripts/package.sh --target android --variant debug -- --no-daemon
scripts/package.sh --target android --variant release -- --no-daemon
```

Use `capno_package` for a command preview or execution when available.

## Context Sync

When Android behavior changes, update only the related context:

- UI/page/state changes: `context/docs/app-ui-workflows/**`.
- BLE, protocol, storage, PDF, printing, alarm, data flow: `context/docs/core-data-protocol/**`.
- Gradle, Manifest, resources, SDK/dependency changes: `context/docs/platform-resources/**` and `context/docs/build-platform.md`.
- Entity path or ID changes: `context/entity-id-mapping.md` and, when relevant, `context/实体标识映射.md`.
