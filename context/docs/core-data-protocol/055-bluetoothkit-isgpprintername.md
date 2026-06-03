# BlueToothKit.isGPPrinterName

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L309）。
领域：蓝牙/打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.isGPPrinterName`
- ID / 别名：GP printer name match, 佳博打印机识别
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：使用 `^GP-(.+)_([\\w\\d\\p{Punct}]+)$` 匹配打印机名称

## 补充职责

判断佳博打印机名称。

## 关键 ID / 别名

- 定位别名：GP printer name match, 佳博打印机识别
- 关键字段 / 方法：Regex `^GP-(.+)_([\\w\\d\\p{Punct}]+)$`。

## 关键字段 / 方法

- 主要字段、方法或协议值：Regex `^GP-(.+)_([\\w\\d\\p{Punct}]+)$`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`autoConnectPrinter` 前置判断。

## 注意事项

名称为空的设备已提前过滤。

## 最小验证方式

`rg "isGPPrinterName"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
