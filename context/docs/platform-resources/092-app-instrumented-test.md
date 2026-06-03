# app instrumented test

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L512）。
领域：测试。
实体级上下文：`context/docs/platform-resources/092-app-instrumented-test.md`。

## 实体定位

- 实体：app instrumented test
- ID / 别名：ExampleInstrumentedTest, app 仪器测试
- 源文件：`app/src/androidTest/java/com/wldmedical/capnoeasy/ExampleInstrumentedTest.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app Android 测试占位文件

## 补充职责

app Android instrumentation 测试入口。

## 关键 ID / 别名

ExampleInstrumentedTest, app 仪器测试

## 关键字段 / 方法

`app/src/androidTest/java/com/wldmedical/capnoeasy/ExampleInstrumentedTest.kt`；runner `AndroidJUnit4`。

## 主要调用点

当前 `useAppContext` 断言 target package 为 `com.wldmedical.capnoeasy`。

## 注意事项

需要连接设备/模拟器；适合验证权限、Activity、Compose UI 和 Android framework 行为。

## 最小验证方式

`./gradlew :app:connectedDebugAndroidTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
