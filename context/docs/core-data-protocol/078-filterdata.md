# filterData

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L332）。
领域：PDF/打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`filterData`
- ID / 别名：waveform filter, 波形过滤
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：移除长段低值点并截取最近波形，供 PDF/打印使用

## 补充职责

过滤长段低值点，并截取最近数据。

## 关键 ID / 别名

- 定位别名：waveform filter, 波形过滤
- 关键字段 / 方法：`consecutiveThreshold = 10`、`lowValuePercentage = 0.1f`、`takeLast(maxXPoints * 2)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`consecutiveThreshold = 10`、`lowValuePercentage = 0.1f`、`takeLast(maxXPoints * 2)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`

## 主要调用点

PDF 导出、热敏打印。

## 注意事项

小于最大值 10% 的连续低值超过 10 个后丢弃。

## 最小验证方式

检查 `PDFKit.kt`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
