# Printer

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L237）。
领域：打印。
聚合章节：Kit 与服务。

## 实体定位

- 实体：Printer
- ID / 别名：printer object, 打印机对象
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：打印辅助对象

## 补充职责

GPrinter SDK 端口包装 object。

## 关键 ID / 别名

- 定位别名：printer object, 打印机对象
- 关键字段 / 方法：`PortManager`、`connect`、`getConnectState`、`sendDataToPrinter`、`close`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`PortManager`、`connect`、`getConnectState`、`sendDataToPrinter`、`close`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`HotmeltPinter`。

## 注意事项

`connect` 在新线程里先 `closePort`，sleep 2 秒后打开蓝牙端口。

## 最小验证方式

检查 `Printer.connect`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
