# Android Gradle Plugin

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L52）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/019-android-gradle-plugin.md`。

## 实体定位

- 实体：Android Gradle Plugin
- ID / 别名：`agp=8.8.0`, `com.android.application`, `com.android.library`, AGP
- 源文件：`gradle/libs.versions.toml`, `build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 与 hotmeltprint 的 Android 插件版本

## 补充职责

Android application/library 构建插件版本来源。

## 关键 ID / 别名

`agp=8.8.0`, `com.android.application`, `com.android.library`, AGP

## 关键字段 / 方法

`agp=8.8.0`；插件 ID 为 `com.android.application` 和 `com.android.library`。

## 主要调用点

`app` 应用 application 插件，`hotmeltprint` 应用 library 插件；根 build 通过 catalog 统一声明。

## 注意事项

升级 AGP 需同时关注 compileSdk、Kotlin/Compose plugin、Gradle wrapper 兼容性。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
