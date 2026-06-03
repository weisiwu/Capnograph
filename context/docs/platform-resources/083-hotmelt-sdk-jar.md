# Hotmelt SDK JAR

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L493）。
领域：依赖。
实体级上下文：`context/docs/platform-resources/083-hotmelt-sdk-jar.md`。

## 实体定位

- 实体：Hotmelt SDK JAR
- ID / 别名：`SDKLib.jar`, 热敏打印 SDK
- 源文件：`hotmeltprint/libs/SDKLib.jar`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：内置热敏打印 SDK JAR

## 补充职责

内置热敏打印 SDK JAR 二进制资源。

## 关键 ID / 别名

`SDKLib.jar`, 热敏打印 SDK

## 关键字段 / 方法

`hotmeltprint/libs/SDKLib.jar`，约 432K。

## 主要调用点

通过 hotmeltprint 的 fileTree 和 `files("libs/SDKLib.jar")` 引入；`HotmeltPinter.kt` 调用 `com.gprinter.*`。

## 注意事项

与三方依赖实体 `GPrinter SDK JAR` 指向同一二进制；打印必须真机验证。

## 最小验证方式

`./gradlew :hotmeltprint:assembleDebug`; 真机连接打印机

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
