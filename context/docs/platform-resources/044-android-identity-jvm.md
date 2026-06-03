# Android Identity JVM

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L449）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/044-android-identity-jvm.md`。

## 实体定位

- 实体：Android Identity JVM
- ID / 别名：`com.android.identity:identity-jvm:202411.1`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的 identity JVM 库

## 补充职责

Android Identity JVM 库，当前由 app 声明。

## 关键 ID / 别名

`com.android.identity:identity-jvm:202411.1`

## 关键字段 / 方法

`com.android.identity:identity-jvm:202411.1`；alias `libs.identity.jvm`。

## 主要调用点

源码未检出 `identity` 直接调用。

## 注意事项

移除或升级前先确认是否有计划中的证件/身份能力依赖。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
