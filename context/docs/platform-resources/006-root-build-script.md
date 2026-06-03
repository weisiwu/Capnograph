# Root build script

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L29）。
领域：构建。
实体级上下文：`context/docs/platform-resources/006-root-build-script.md`。

## 实体定位

- 实体：Root build script
- ID / 别名：project plugins, 根构建脚本
- 源文件：`build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：根工程 Gradle Kotlin DSL 配置

## 补充职责

根工程插件声明入口，集中声明 Android application/library、Kotlin Android/Compose/JVM 和 Hilt 插件版本。

## 关键 ID / 别名

project plugins, 根构建脚本

## 关键字段 / 方法

Android/Kotlin 插件通过 version catalog `apply false`；Hilt 插件直接声明 `id("com.google.dagger.hilt.android") version "2.51.1" apply false`。

## 主要调用点

`app/build.gradle.kts` 应用 application、Kotlin Android、Compose、Hilt、kapt；`hotmeltprint` 应用 Android library 和 Kotlin Android。

## 注意事项

根 `buildscript.dependencies` 当前为空；Kotlin JVM 插件只声明未应用。

## 最小验证方式

`./gradlew help` 可验证插件解析。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
