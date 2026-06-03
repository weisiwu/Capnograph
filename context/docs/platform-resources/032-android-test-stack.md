# Android test stack

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L65）。
领域：测试。
实体级上下文：`context/docs/platform-resources/032-android-test-stack.md`。

## 实体定位

- 实体：Android test stack
- ID / 别名：`junit=4.13.2`, `androidx.test.ext:junit=1.2.1`, `espresso-core=3.6.1`, `ui-test-junit4`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：JVM、Android instrumentation 和 Compose UI 测试依赖

## 补充职责

JVM、Android instrumentation 和 Compose UI 测试依赖集合。

## 关键 ID / 别名

`junit=4.13.2`, `androidx.test.ext:junit=1.2.1`, `espresso-core=3.6.1`, `ui-test-junit4`

## 关键字段 / 方法

JUnit 4.13.2、AndroidX Test JUnit 1.2.1、Espresso 3.6.1、Compose `ui-test-junit4` 和 `ui-test-manifest`。

## 主要调用点

四个 Example 测试文件当前是占位测试；app debug 包含 Compose test manifest。

## 注意事项

instrumentation 测试需要连接设备/模拟器；当前没有实际 Compose UI 测试文件。

## 最小验证方式

`./gradlew :app:testDebugUnitTest :hotmeltprint:testDebugUnitTest`；设备上跑 `connectedDebugAndroidTest`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
