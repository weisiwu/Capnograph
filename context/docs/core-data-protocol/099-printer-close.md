# Printer.close

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L353）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`Printer.close`
- ID / 别名：close printer port, 关闭打印端口
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`, `hotmeltprint/libs/SDKLib.jar`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：关闭 SDK PortManager 并清空端口引用

## 补充职责

关闭打印端口并清空引用。

## 关键 ID / 别名

- 定位别名：close printer port, 关闭打印端口
- 关键字段 / 方法：`closePort()`、`portManager = null`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`closePort()`、`portManager = null`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`, `hotmeltprint/libs/SDKLib.jar`

## 主要调用点

打印机断开/重连。

## 注意事项

无连接时安全返回。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
