# Android SDK levels

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L59）。
领域：Android 平台。
实体级上下文：`context/docs/platform-resources/026-android-sdk-levels.md`。

## 实体定位

- 实体：Android SDK levels
- ID / 别名：`compileSdk=35`, `targetSdk=35`, `app minSdk=30`, `hotmeltprint minSdk=24`
- 源文件：`app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：以模块 build 文件为准

## 补充职责

记录两个 Android 模块当前 SDK 兼容范围。

## 关键 ID / 别名

`compileSdk=35`, `targetSdk=35`, `app minSdk=30`, `hotmeltprint minSdk=24`

## 关键字段 / 方法

两个模块 `compileSdk=35`；app `minSdk=30`、`targetSdk=35`；hotmeltprint `minSdk=24`。

## 主要调用点

影响 API 可用性、Manifest 权限行为、运行时权限和商店目标 SDK 要求。

## 注意事项

旧项目记忆可能写 app minSdk 33；当前代码事实以 build 文件的 30 为准。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
