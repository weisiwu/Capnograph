# Version catalog

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L28）。
领域：构建。
实体级上下文：`context/docs/platform-resources/005-version-catalog.md`。

## 实体定位

- 实体：Version catalog
- ID / 别名：libraries, plugins, 版本目录
- 源文件：`gradle/libs.versions.toml`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：依赖和插件版本

## 补充职责

集中声明 Gradle 插件版本和多数依赖别名，供根工程和模块 build 文件通过 `libs.*` 引用。

## 关键 ID / 别名

libraries, plugins, 版本目录

## 关键字段 / 方法

`agp=8.8.0`、`kotlin=2.0.0`、`composeBom=2024.04.01`、`coreKtx=1.15.0`、`appcompat=1.7.0`、`hiltAndroid=2.51.1`、`mpandroidchart=v3.1.0`。

## 主要调用点

`build.gradle.kts` 使用 `libs.plugins.*`；模块 build 文件使用 `libs.androidx.*`、`libs.material`、`libs.hilt.android` 等依赖别名。

## 注意事项

`firebase-config-ktx` 目前只在 catalog 定义，未被模块 build 文件引用；升级 catalog 后需要确认模块引用是否同步。

## 最小验证方式

`./gradlew :app:dependencies --configuration debugRuntimeClasspath` 可核对解析版本。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
