# Kotlin Compose plugin

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L56）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/023-kotlin-compose-plugin.md`。

## 实体定位

- 实体：Kotlin Compose plugin
- ID / 别名：`org.jetbrains.kotlin.plugin.compose`, `libs.plugins.kotlin.compose`, Compose compiler
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块启用 Compose 编译插件

## 补充职责

app 模块 Compose compiler 插件。

## 关键 ID / 别名

`org.jetbrains.kotlin.plugin.compose`, `libs.plugins.kotlin.compose`, Compose compiler

## 关键字段 / 方法

插件 ID `org.jetbrains.kotlin.plugin.compose`，版本跟随 Kotlin `2.0.0`；app 启用 `buildFeatures.compose=true`。

## 主要调用点

编译 `pages`、`components` 和 `ui.theme` 中的 `@Composable` 函数。

## 注意事项

hotmeltprint 模块声明 foundation 依赖但未启用 Compose plugin；目前 Compose UI 主要在 app 模块。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
