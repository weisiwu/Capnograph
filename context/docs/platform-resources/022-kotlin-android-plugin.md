# Kotlin Android plugin

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L55）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/022-kotlin-android-plugin.md`。

## 实体定位

- 实体：Kotlin Android plugin
- ID / 别名：`kotlin=2.0.0`, `org.jetbrains.kotlin.android`, `libs.plugins.kotlin.android`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：两个 Android 模块的 Kotlin 插件

## 补充职责

两个 Android 模块的 Kotlin 编译插件。

## 关键 ID / 别名

`kotlin=2.0.0`, `org.jetbrains.kotlin.android`, `libs.plugins.kotlin.android`

## 关键字段 / 方法

`kotlin=2.0.0`，插件 ID `org.jetbrains.kotlin.android`；两个模块都设置 `jvmTarget="11"`。

## 主要调用点

编译所有 `app/src/main/java` 和 `hotmeltprint/src/main/java` 下的 Kotlin 源码。

## 注意事项

Kotlin 版本还驱动 Compose compiler plugin 版本；升级需回归 kapt、Compose 和 Android Gradle Plugin 兼容。

## 最小验证方式

`./gradlew :app:compileDebugKotlin :hotmeltprint:compileDebugKotlin`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
