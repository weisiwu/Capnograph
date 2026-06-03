# HotmeltPinter.connect

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L355）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`HotmeltPinter.connect`
- ID / 别名：connect printer by mac, MAC 连接打印机
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：构造 GPrinter Bluetooth PrinterDevices 并连接

## 补充职责

按 MAC 构造 GPrinter 蓝牙设备并连接。

## 关键 ID / 别名

- 定位别名：connect printer by mac, MAC 连接打印机
- 关键字段 / 方法：`PrinterDevices.Build`、`ConnMethod.BLUETOOTH`、`Command.ESC`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`PrinterDevices.Build`、`ConnMethod.BLUETOOTH`、`Command.ESC`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`BlueToothKit.autoConnectPrinter`。

## 注意事项

保存 `macAddress`。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
