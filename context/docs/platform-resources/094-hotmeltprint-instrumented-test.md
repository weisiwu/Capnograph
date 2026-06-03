# hotmeltprint instrumented test

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L514）。
领域：测试。
实体级上下文：`context/docs/platform-resources/094-hotmeltprint-instrumented-test.md`。

## 实体定位

- 实体：hotmeltprint instrumented test
- ID / 别名：ExampleInstrumentedTest, hotmeltprint 仪器测试
- 源文件：`hotmeltprint/src/androidTest/java/com/wldmedical/hotmeltprint/ExampleInstrumentedTest.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：hotmeltprint Android 测试占位文件

## 补充职责

hotmeltprint Android instrumentation 测试入口。

## 关键 ID / 别名

ExampleInstrumentedTest, hotmeltprint 仪器测试

## 关键字段 / 方法

`hotmeltprint/src/androidTest/java/com/wldmedical/hotmeltprint/ExampleInstrumentedTest.kt`；runner `AndroidJUnit4`。

## 主要调用点

当前 `useAppContext` 断言 target package 为 `com.wldmedical.hotmeltprint.test`。

## 注意事项

需要连接设备/模拟器；打印 SDK 实际连接仍需打印机硬件。

## 最小验证方式

`./gradlew :hotmeltprint:connectedDebugAndroidTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
