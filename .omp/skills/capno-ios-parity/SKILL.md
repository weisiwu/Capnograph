---
name: capno-ios-parity
description: iOS SwiftUI and cross-platform parity guidance for CapnoGraph. Use for iOS files, SwiftUI views, CoreBluetooth flow, app config, history data, or keeping iOS behavior aligned with Android context.
globs:
  - "apps/ios/**"
  - "context/文档/实体/**"
  - "context/项目上下文.md"
---

# CapnoGraph iOS Parity Skill

Use this skill for iOS work in `apps/ios` and for cross-platform behavior alignment.

## Project Facts

- Xcode project: `apps/ios/CapnoGraph.xcodeproj`.
- Scheme: `CapnoGraph`.
- Bundle identifier: `WLD.CapnoEasy`.
- Deployment target: iOS 17.0.
- Main SwiftUI app: `apps/ios/CapnoGraph/CapnoGraphApp.swift`.
- Main screens include `ContentView`, `SplashView`, `SearchDeviceListView`, `ConfigView`, `AlertConfigView`, `DisplayConfigView`, `ModuleConfigView`, `SystemConfigView`, and `ResultView`.
- Core managers include `BluetoothManage.swift`, `AppConfigManage.swift`, and `HistoryDataManage.swift`.

## Cross-Platform Alignment

- For concepts that also exist on Android, compare against `context/entity-id-mapping.md`, `context/docs/app-ui-workflows/**`, and `context/docs/core-data-protocol/**`.
- Keep CO2 unit, scale, WF speed, alarm range, no-breath time, O2 compensation, BLE command semantics, and localized strings consistent unless the platform intentionally differs.
- When adding iOS-only behavior, document the difference explicitly in the relevant context note.

## Validation

From the repository root:

```bash
scripts/package.sh --target ios --variant debug
scripts/package.sh --target ios --variant release --export-options-plist ExportOptions.plist
```

For targeted Xcode validation, run from `apps/ios` and keep DerivedData under `apps/ios/build/DerivedData` unless there is a reason to use a local path.

## Context Sync

- Swift entity changes usually map through `context/实体标识映射.md` and `context/文档/实体/<ID>.md`.
- Cross-platform behavior changes may also need Android-oriented context files when they describe shared device protocol or shared product behavior.
- Do not update generated context wholesale; patch only affected entities or append manual notes outside managed blocks.
