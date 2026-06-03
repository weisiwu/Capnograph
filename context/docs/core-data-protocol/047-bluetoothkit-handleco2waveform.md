# BlueToothKit.handleCO2Waveform

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L301）。
领域：协议函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.handleCO2Waveform`
- ID / 别名：parse 80H waveform, 解析波形
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：解析 CO2、ETCO2、RR、FiCO2、呼吸状态并更新报警和记录缓存

## 补充职责

解析 80H 波形、状态、ETCO2/RR/FiCO2，并更新报警与记录缓存。

## 关键 ID / 别名

- 定位别名：parse 80H waveform, 解析波形
- 关键字段 / 方法：`ISBState80H`、`currentCO2`、`currentETCO2`、`currentRespiratoryRate`、`updateTotalCO2WavedData`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`ISBState80H`、`currentCO2`、`currentETCO2`、`currentRespiratoryRate`、`updateTotalCO2WavedData`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`getSpecificValue`。

## 注意事项

`FiCO2` 公式里 `and` 与乘法优先级需谨慎；文档不改代码。

## 最小验证方式

检查 80H 分支

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
