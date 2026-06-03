# Core AndroidX libraries

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L66）。
领域：Android 平台。
实体级上下文：`context/docs/platform-resources/033-core-androidx-libraries.md`。

## 实体定位

- 实体：Core AndroidX libraries
- ID / 别名：`core-ktx`, `appcompat`, `activity-compose`, `lifecycle-runtime-ktx`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：两个模块共享或 app 使用的 AndroidX 基础库

## 补充职责

基础 AndroidX 依赖集合，覆盖 core-ktx、appcompat、activity-compose 和 lifecycle-runtime-ktx。

## 关键 ID / 别名

`core-ktx`, `appcompat`, `activity-compose`, `lifecycle-runtime-ktx`

## 关键字段 / 方法

`core-ktx=1.15.0`、`appcompat=1.7.0`、`activity-compose=1.9.3`、`lifecycle-runtime-ktx=2.8.7`。

## 主要调用点

core/appcompat 被两个模块声明；activity/lifecycle 主要支撑 app Activity 与 Compose 生命周期。

## 注意事项

AppCompat 语言切换是明确调用点；其他基础库主要作为平台扩展和运行时支撑。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
