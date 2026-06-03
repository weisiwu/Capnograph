# Gradle version catalog

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L50）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/017-gradle-version-catalog.md`。

## 实体定位

- 实体：Gradle version catalog
- ID / 别名：`libs.versions.toml`, `libs.*`, `libs.plugins.*`, 版本目录
- 源文件：`gradle/libs.versions.toml`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：插件和多数依赖的集中别名与版本来源

## 补充职责

版本目录能力，统一 `libs.versions.toml` 中的 `[versions]`、`[libraries]` 和 `[plugins]`。

## 关键 ID / 别名

`libs.versions.toml`, `libs.*`, `libs.plugins.*`, 版本目录

## 关键字段 / 方法

插件别名包括 `android-application`、`android-library`、`kotlin-android`、`kotlin-compose`、`jetbrains-kotlin-jvm`；库别名覆盖 AndroidX、Compose、Material、Hilt、MPAndroidChart 等。

## 主要调用点

根 build 使用 `libs.plugins.*`；模块 dependencies 使用 `libs.androidx.*`、`libs.material`、`libs.hilt.android`。

## 注意事项

不是所有依赖都进 catalog：wheel picker、core-splashscreen、Room、Gson、iTextPDF、AndroidPdfViewer、Coil、Accompanist 仍在模块 build 文件硬编码。

## 最小验证方式

`./gradlew :app:dependencies --configuration debugRuntimeClasspath`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
