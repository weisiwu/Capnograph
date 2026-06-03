# GPrinter SDK JAR

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L470）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/065-gprinter-sdk-jar.md`。

## 实体定位

- 实体：GPrinter SDK JAR
- ID / 别名：`SDKLib.jar`, `com.gprinter.*`
- 源文件：`hotmeltprint/libs/SDKLib.jar`, `hotmeltprint/build.gradle.kts`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：热敏打印 SDK，提供 BluetoothPort、EscCommand、PrinterDevices 等

## 补充职责

本地 GPrinter 热敏打印 SDK JAR。

## 关键 ID / 别名

`SDKLib.jar`, `com.gprinter.*`

## 关键字段 / 方法

`hotmeltprint/libs/SDKLib.jar`，约 432K；提供 `com.gprinter.*` API。

## 主要调用点

`HotmeltPinter.kt` 使用 `BluetoothPort`、`PortManager`、`EscCommand`、`PrinterDevices` 等。

## 注意事项

`hotmeltprint/build.gradle.kts` 同时 fileTree 和 `files("libs/SDKLib.jar")` 引入，变更时注意重复。

## 最小验证方式

`./gradlew :hotmeltprint:assembleDebug`；真机连接打印机验证

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
