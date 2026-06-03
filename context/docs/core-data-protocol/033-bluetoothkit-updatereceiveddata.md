# BlueToothKit.updateReceivedData

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L287）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateReceivedData`
- ID / 别名：update dataFlow, 更新实时数据流
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：维护最多 `maxXPoints` 个 DataPoint 的 StateFlow

## 补充职责

维护 UI 实时图表数据流。

## 关键 ID / 别名

- 定位别名：update dataFlow, 更新实时数据流
- 关键字段 / 方法：`_dataFlow`、`maxXPoints`、`DataPoint`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`_dataFlow`、`maxXPoints`、`DataPoint`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`handleCO2Waveform`。

## 注意事项

达到 500 点后移除头部。

## 最小验证方式

检查 `_dataFlow.value.size >= maxXPoints`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
