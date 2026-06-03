# AndroidX Lifecycle Runtime KTX

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L440）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/035-androidx-lifecycle-runtime-ktx.md`。

## 实体定位

- 实体：AndroidX Lifecycle Runtime KTX
- ID / 别名：`androidx.lifecycle:lifecycle-runtime-ktx:2.8.7`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Lifecycle 协程/运行时基础

## 补充职责

Lifecycle 协程/运行时基础，支撑 app 生命周期感知能力。

## 关键 ID / 别名

`androidx.lifecycle:lifecycle-runtime-ktx:2.8.7`

## 关键字段 / 方法

`androidx.lifecycle:lifecycle-runtime-ktx:2.8.7`；alias `libs.androidx.lifecycle.runtime.ktx`。

## 主要调用点

`app/build.gradle.kts` 声明；Activity/Compose 生命周期运行时依赖它。

## 注意事项

当前未单独记录显式 API 调用，属于 app 运行时基础库。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
