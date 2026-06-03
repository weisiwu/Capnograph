# SaveChartToPdfTask.addWaveformSections

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L338）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.addWaveformSections`
- ID / 别名：report waveform sections, PDF 报告波形段
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将最近最多三段 14 秒 CO2 波形加入纸质报告单

## 补充职责

构建最近最多三段 14 秒波形，按时间顺序向 PDF 添加测量时间、`12.5mm/s`、手绘波形图和指标行。

## 关键 ID / 别名

- 定位别名：report waveform sections, PDF 报告波形段
- 关键字段 / 方法：`REPORT_SEGMENT_SECONDS=14`、`REPORT_MAX_SEGMENTS=3`、`buildReportSegments`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`REPORT_SEGMENT_SECONDS=14`、`REPORT_MAX_SEGMENTS=3`、`buildReportSegments`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.savePDF`。

## 注意事项

测量时间通过 `record.startTime + pointIndex / POINTS_PER_SECOND` 推算；历史数据少于 14 秒时仍输出一段短波形。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查最多三段波形。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
