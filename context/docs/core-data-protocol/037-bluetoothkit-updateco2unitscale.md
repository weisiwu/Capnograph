# BlueToothKit.updateCO2UnitScale

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L291）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateCO2UnitScale`
- ID / 别名：update display config, 更新单位量程
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：排队更新 CO2 单位和 CO2 量程

## 补充职责

排队更新 CO2 单位和量程。

## 关键 ID / 别名

- 定位别名：update display config, 更新单位量程
- 关键字段 / 方法：`co2Unit`、`co2Scale`、`wfSpeed`、`callback`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`co2Unit`、`co2Scale`、`wfSpeed`、`callback`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

显示设置保存。

## 注意事项

`wfSpeed` 参数当前未下发。

## 最小验证方式

检查函数体

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
