# BlueToothKit.autoConnectPrinter

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L308）。
领域：蓝牙/打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.autoConnectPrinter`
- ID / 别名：auto connect GP printer, 自动连接打印机
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：扫描经典蓝牙时自动连接匹配的佳博打印机

## 补充职责

自动连接扫描到的 GP 打印机。

## 关键 ID / 别名

- 定位别名：auto connect GP printer, 自动连接打印机
- 关键字段 / 方法：`connectedPrinter`、`gpPrinterManager.connect(device.address)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`connectedPrinter`、`gpPrinterManager.connect(device.address)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

classic 扫描广播 `ACTION_FOUND`。

## 注意事项

只在当前未连接打印机时尝试。

## 最小验证方式

检查 `discoveryReceiver`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
