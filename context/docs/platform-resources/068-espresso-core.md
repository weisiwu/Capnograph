# Espresso Core

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L473）。
领域：测试依赖。
实体级上下文：`context/docs/platform-resources/068-espresso-core.md`。

## 实体定位

- 实体：Espresso Core
- ID / 别名：`androidx.test.espresso:espresso-core:3.6.1`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Android UI/instrumentation 测试依赖

## 补充职责

Android UI/instrumentation 测试依赖。

## 关键 ID / 别名

`androidx.test.espresso:espresso-core:3.6.1`

## 关键字段 / 方法

`androidx.test.espresso:espresso-core:3.6.1`。

## 主要调用点

两个模块声明；当前示例测试未直接使用 Espresso API。

## 注意事项

新增 View UI 测试时可使用；Compose UI 测试另有 `ui-test-junit4`。

## 最小验证方式

`./gradlew :app:connectedDebugAndroidTest :hotmeltprint:connectedDebugAndroidTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
