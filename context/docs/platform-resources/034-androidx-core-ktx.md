# AndroidX Core KTX

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L439）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/034-androidx-core-ktx.md`。

## 实体定位

- 实体：AndroidX Core KTX
- ID / 别名：`androidx.core:core-ktx:1.15.0`, coreKtx
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Android Kotlin 扩展基础库

## 补充职责

Android Kotlin 扩展基础库，供 app 和 hotmeltprint 使用。

## 关键 ID / 别名

`androidx.core:core-ktx:1.15.0`, coreKtx

## 关键字段 / 方法

`androidx.core:core-ktx:1.15.0`；catalog alias `libs.androidx.core.ktx`。

## 主要调用点

两个模块 dependencies 声明；源码可使用 Android KTX 扩展方法。

## 注意事项

基础依赖，升级时同时编译两个模块。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
