# HotmeltPinter

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L236）。
领域：打印。
聚合章节：Kit 与服务。

## 实体定位

- 实体：HotmeltPinter
- ID / 别名：thermal printer, 热敏打印
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：热敏打印输出库入口

## 补充职责

热敏打印业务封装。

## 关键 ID / 别名

- 定位别名：thermal printer, 热敏打印
- 关键字段 / 方法：`connect`、`print`、`generateWaveformBitmapNew`、`startProcessingData`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`connect`、`print`、`generateWaveformBitmapNew`、`startProcessingData`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`BlueToothKit.autoConnectPrinter`、`HistoryRecordDetailActivity.onPrintTicketClick`。

## 注意事项

类名拼写为 `Pinter`，保持代码一致。

## 最小验证方式

`rg "HotmeltPinter"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
