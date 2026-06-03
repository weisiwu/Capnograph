# SaveChartToPdfTask.addWaveformSections

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.addWaveformSections`
- ID / 别名：report waveform sections, PDF 报告异常上下文波形
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：长记录报告方案 1
- 备注：按 EtCO2/RR 报告阈值输出异常上下文 CO2 波形，默认 60 秒上下文且可配置为 10-300 秒

## 补充职责

构建 PDF 的“异常片段”section。函数调用 `buildAbnormalReportSegments` 从记录数据里识别 EtCO2/RR 异常事件，再用 `buildAbnormalWindows` 生成上下文窗口，最后添加段标题、测量时间、异常时间、手绘 CO2 波形图和段统计。

## 关键 ID / 别名

- 定位别名：report waveform sections, PDF 报告异常上下文波形
- 关键字段 / 方法：`buildAbnormalReportSegments`、`buildAbnormalEvents`、`buildAbnormalWindows`、`resolveEventContextSeconds`、`abnormalReasons`、`addWaveformHeader`、`addWaveformChart`、`addWaveformMetrics`

## 关键字段 / 方法

- 主要字段、方法或协议值：`PrintSetting.DEFAULT_PDF_EVENT_CONTEXT_SECONDS=60`、`PrintSetting.MIN_PDF_EVENT_CONTEXT_SECONDS=10`、`PrintSetting.MAX_PDF_EVENT_CONTEXT_SECONDS=300`、`templateConfig.abnormalEtco2LowMmHg=25`、`templateConfig.abnormalEtco2HighMmHg=50`、`templateConfig.abnormalRrLow=5`、`templateConfig.abnormalRrHigh=30`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.savePDF` 在全程趋势之后调用。

## 注意事项

当前没有真实报警事件流和报警阈值快照，PDF 端暂时从已存 `CO2WavePointData.ETCO2` 和 `RR` 回算报告异常。上下文秒数来自 `PrintSetting.pdfEventContextSeconds`，缺省使用 60 秒，保存和读取时都裁剪到 `10..300`。如果没有异常，section 仍会输出“未检测到超过报告阈值的 EtCO2/RR 异常”和阈值说明。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出含正常记录和异常记录的 PDF，检查无异常提示、异常原因、异常时间、上下文窗口长度和段统计。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
