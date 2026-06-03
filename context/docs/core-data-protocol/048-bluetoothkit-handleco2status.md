# BlueToothKit.handleCO2Status

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L302）。
领域：协议函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.handleCO2Status`
- ID / 别名：parse CO2 status, 解析 CO2 状态
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：处理校零状态和窒息状态

## 补充职责

处理 80H CO2 工作状态中的校零和窒息位。

## 关键 ID / 别名

- 定位别名：parse CO2 status, 解析 CO2 状态
- 关键字段 / 方法：`ZSBState`、`0x0C`、`0x40`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`ZSBState`、`0x0C`、`0x40`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`handleCO2Waveform` 的 `CO2WorkStatus` 分支。

## 注意事项

校零回调只在从 Resetting 回到 NOZeroning 时触发。

## 最小验证方式

`rg "ZSBM"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
