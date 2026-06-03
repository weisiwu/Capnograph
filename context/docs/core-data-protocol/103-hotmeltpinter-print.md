# HotmeltPinter.print

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L357）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`HotmeltPinter.print`
- ID / 别名：print report ticket, 打印报告小票
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：输出医院/报告/患者信息，切纸后打印波形图

## 补充职责

打印报告小票头部并切纸，然后打印波形图。

## 关键 ID / 别名

- 定位别名：print report ticket, 打印报告小票
- 关键字段 / 方法：`EscCommand`、`PrintSetting`、`addCutPaper`、`startProcessingData`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`EscCommand`、`PrintSetting`、`addCutPaper`、`startProcessingData`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`HistoryRecordDetailActivity.onPrintTicketClick`。

## 注意事项

`esc` 是实例字段，连续打印前只调用 `addInitializePrinter`，需留意指令缓存状态。

## 最小验证方式

检查 `esc` 使用

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
