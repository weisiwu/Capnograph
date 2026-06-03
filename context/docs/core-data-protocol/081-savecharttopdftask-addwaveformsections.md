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
- 备注：优先按真实采样时间从记录开头加入最多三段 14 秒 CO2 波形，最后一段可不足 14 秒

## 补充职责

从记录开头构建最多三段连续 14 秒波形，按时间顺序向 PDF 添加测量时间、手绘波形图和分段统计；新记录优先用 `CO2WavePointData.sampleTimeMillis` 切分真实 14 秒窗口，写死的 `12.5mm/s` 速度文案已删除。

## 关键 ID / 别名

- 定位别名：report waveform sections, PDF 报告波形段
- 关键字段 / 方法：`templateConfig.segmentSeconds=14`、`templateConfig.segmentMillis`、`templateConfig.maxSegments=3`、`buildReportSegments`、`buildTimedReportSegments`、`buildIndexedReportSegments`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`templateConfig.segmentSeconds=14`、`templateConfig.segmentMillis`、`templateConfig.maxSegments=3`、`buildReportSegments`、`buildTimedReportSegments`、`buildIndexedReportSegments`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.savePDF`。

每段统计行显示 EtCO2、FiCO2、RR 的均值/最大/最小/段末，避免把段指标误解为稳定值。

## 注意事项

新记录测量时间通过 `sampleTimeMillis` 转本地时间显示，并按真实时间窗口显示 0-14、14-28 这种连续范围；旧记录或混合缺失时间戳的数据仍通过 `record.startTime + pointIndex / POINTS_PER_SECOND` 兜底。历史数据少于 14 秒时仍输出一段短波形，数据不足 42 秒时只有最后一段不足 14 秒。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查最多三段波形。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
