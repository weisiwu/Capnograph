# Kotlin JVM plugin

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L58）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/025-kotlin-jvm-plugin.md`。

## 实体定位

- 实体：Kotlin JVM plugin
- ID / 别名：`org.jetbrains.kotlin.jvm`, `jetbrains-kotlin-jvm=2.0.0`
- 源文件：`gradle/libs.versions.toml`, `build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：根构建脚本声明但当前没有模块应用

## 补充职责

根工程声明的 JVM Kotlin 插件能力。

## 关键 ID / 别名

`org.jetbrains.kotlin.jvm`, `jetbrains-kotlin-jvm=2.0.0`

## 关键字段 / 方法

`org.jetbrains.kotlin.jvm`，version catalog 别名 `jetbrains-kotlin-jvm=2.0.0`。

## 主要调用点

当前没有模块应用，仅作为可用插件声明。

## 注意事项

新增纯 JVM 模块时才会实际使用；当前 Android 模块不依赖它。

## 最小验证方式

`./gradlew help`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
