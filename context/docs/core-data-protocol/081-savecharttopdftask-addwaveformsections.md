# SaveChartToPdfTask.addWaveformSections

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.addWaveformSections`
- ID / 别名：report waveform sections, PDF 报告连续波形段
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：长记录报告方案 1
- 备注：按完整记录时间轴输出连续 CO2 波形，每段固定 15 秒

## 补充职责

构建 PDF 的连续波形 section。函数调用 `buildWaveformSegments` 按完整记录时间轴把 CO2 数据切成固定 15 秒窗口，最后添加测量时间、手绘 CO2 波形图和段统计，不再显示“波形 1/波形 2”这类段标题。

## 关键 ID / 别名

- 定位别名：report waveform sections, PDF 报告连续波形段
- 关键字段 / 方法：`buildWaveformSegments`、`waveformSegmentDurationSeconds`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`

## 关键字段 / 方法

- 主要字段、方法或协议值：`templateConfig.waveformSegmentDurationSeconds=15`、`buildWaveformSegments`、`pointTimelineMillis`、`displayTimelineMillis`、`timelineMillisToPointIndex`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.savePDF` 在基础信息之后调用。

## 注意事项

当前 PDF 不再从 `CO2WavePointData.ETCO2` 和 `RR` 回算报告异常，也不输出无异常提示或阈值说明。切段优先使用 `sampleTimeMillis` 真实采样时间；旧数据无时间戳时回退到 index 和 `POINTS_PER_SECOND`。`PrintSetting.pdfEventContextSeconds` 仍可保存，但当前导出固定使用 15 秒连续波形段。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出长记录 PDF，检查全记录按 15 秒连续切段、测量时间、波形和段统计。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
