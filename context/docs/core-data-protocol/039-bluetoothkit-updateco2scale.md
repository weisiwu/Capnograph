# BlueToothKit.updateCO2Scale

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L293）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateCO2Scale`
- ID / 别名：set CO2 scale, 设置量程
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：发送 Expand/CO2Scale 指令

## 补充职责

构造 Expand/CO2Scale 命令。

## 关键 ID / 别名

- 定位别名：set CO2 scale, 设置量程
- 关键字段 / 方法：`Expand 0xF2`、NBF `0x03`、`ISBStateF2H.CO2Scale = 0x2C`、middle=0、small=1、large=2。

## 关键字段 / 方法

- 主要字段、方法或协议值：`Expand 0xF2`、NBF `0x03`、`ISBStateF2H.CO2Scale = 0x2C`、middle=0、small=1、large=2。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

`updateCO2UnitScale`、初始化命令。

## 注意事项

按量程族映射，不按单位分别下发。

## 最小验证方式

检查 scale list

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
