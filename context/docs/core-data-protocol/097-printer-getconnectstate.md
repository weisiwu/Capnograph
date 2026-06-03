# Printer.getConnectState

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L351）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`Printer.getConnectState`
- ID / 别名：SDK printer state, 打印机连接状态
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`, `hotmeltprint/libs/SDKLib.jar`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：读取 GPrinter 端口连接状态

## 补充职责

读取端口连接状态。

## 关键 ID / 别名

- 定位别名：SDK printer state, 打印机连接状态
- 关键字段 / 方法：`portManager?.getConnectStatus() ?: false`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`portManager?.getConnectStatus() ?: false`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`, `hotmeltprint/libs/SDKLib.jar`

## 主要调用点

详情页打印前检查。

## 注意事项

端口为空返回 false。

## 最小验证方式

`rg "getConnectState"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
