# hotmeltprint module

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L26）。
领域：模块。
实体级上下文：`context/docs/platform-resources/003-hotmeltprint-module.md`。

## 实体定位

- 实体：hotmeltprint module
- ID / 别名：`:hotmeltprint`, `com.wldmedical.hotmeltprint`, 热敏打印模块
- 源文件：`hotmeltprint/build.gradle.kts`, `hotmeltprint/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：热敏打印 SDK 库模块

## 补充职责

Android library 模块，封装本地 GPrinter SDK JAR，并为 app 提供蓝牙热敏打印、图表 bitmap 渲染和 ESC 指令输出。

## 关键 ID / 别名

`:hotmeltprint`, `com.wldmedical.hotmeltprint`, 热敏打印模块

## 关键字段 / 方法

`namespace="com.wldmedical.hotmeltprint"`、`compileSdk=35`、`minSdk=24`；使用 Android library 与 Kotlin Android 插件。

## 主要调用点

`app` 通过 `implementation(project(":hotmeltprint"))` 调用；核心源码是 `HotmeltPinter.kt`。

## 注意事项

Manifest 当前为空；打印权限依赖 app 层声明。`SDKLib.jar` 同时经 fileTree 和 `files("libs/SDKLib.jar")` 声明，调整依赖时注意重复引入。

## 最小验证方式

`./gradlew :hotmeltprint:assembleDebug`；实际打印需真机和 GPrinter 设备。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
