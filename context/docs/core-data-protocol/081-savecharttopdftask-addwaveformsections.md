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
- 备注：识别 EtCO2/RR 异常并加入默认 60 秒、可配置的上下文 CO2 波形

## 补充职责

构建 PDF 的“异常片段”section。函数先输出异常片段标题和判定阈值说明，再调用 `buildAbnormalReportSegments`，按 EtCO2/RR 异常事件生成上下文窗口，向 PDF 添加异常原因、测量时间、手绘 CO2 波形图和分段统计；新记录优先用 `CO2WavePointData.sampleTimeMillis` 定位真实时间窗口，旧记录按 `index / POINTS_PER_SECOND` 兜底。

## 关键 ID / 别名

- 定位别名：report waveform sections, PDF 报告波形段
- 关键字段 / 方法：`resolveEventContextSeconds`、`resolveEventContextMillis`、`buildAbnormalReportSegments`、`buildAbnormalWindows`、`buildAbnormalEvents`、`abnormalReasons`、`abnormalCriteriaText`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`PrintSetting.DEFAULT_PDF_EVENT_CONTEXT_SECONDS=60`、`MIN_PDF_EVENT_CONTEXT_SECONDS=10`、`MAX_PDF_EVENT_CONTEXT_SECONDS=300`、`resolveEventContextSeconds`、`buildAbnormalReportSegments`、`buildAbnormalWindows`、`buildAbnormalEvents`、`abnormalReasons`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.savePDF`。

每段统计行显示 EtCO2、FiCO2、RR 的均值/最大/最小/段末，避免把段指标误解为稳定值。

## 注意事项

异常判定当前来自报告阈值：EtCO2 低于 25mmHg 或高于 50mmHg、RR 低于 5bpm 或高于 30bpm；EtCO2 阈值会按当前 `co2Unit` 换算为 kPa 或 `%`。异常窗口默认 60 秒，由 `PrintSetting.pdfEventContextSeconds` 覆盖并限制在 10-300 秒；窗口以事件中点为中心，靠近记录边界时自动贴边，相互重叠的窗口会合并。没有异常时仍输出一行“未检测到超过报告阈值的 EtCO2/RR 异常”。新记录测量时间通过 `sampleTimeMillis` 转本地时间显示；旧记录或混合缺失时间戳的数据仍通过 `record.startTime + pointIndex / POINTS_PER_SECOND` 兜底。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查全程趋势、异常片段说明和 60 秒上下文波形。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
