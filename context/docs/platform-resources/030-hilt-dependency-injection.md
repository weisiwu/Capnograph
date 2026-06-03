# Hilt dependency injection

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L63）。
领域：依赖注入。
实体级上下文：`context/docs/platform-resources/030-hilt-dependency-injection.md`。

## 实体定位

- 实体：Hilt dependency injection
- ID / 别名：`com.google.dagger.hilt.android`, `hilt-android=2.51.1`, `@HiltAndroidApp`, `@HiltViewModel`
- 源文件：`build.gradle.kts`, `app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`, `app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块应用 Hilt 插件并配置 Hilt compiler

## 补充职责

app 依赖注入基础能力，管理 Application、Activity/ViewModel/Kit 构造注入。

## 关键 ID / 别名

`com.google.dagger.hilt.android`, `hilt-android=2.51.1`, `@HiltAndroidApp`, `@HiltViewModel`

## 关键字段 / 方法

根 build 声明 Hilt Gradle plugin `2.51.1`；app 依赖 `hilt-android` 和 `hilt-android-compiler`；源码使用 `@HiltAndroidApp`、`@HiltViewModel`。

## 主要调用点

`CapnoEasyApplication.kt`、`AppStateModel.kt`、页面/Kit 构造注入链。

## 注意事项

`hotmeltprint` 也声明 `libs.hilt.android`，但没有自己的 Hilt 插件/kapt 配置。

## 最小验证方式

`./gradlew :app:kaptDebugKotlin`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
