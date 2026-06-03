# SaveChartToPdfTask.createWaveformBitmap

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L339）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.createWaveformBitmap`
- ID / 别名：manual waveform bitmap, PDF 手绘波形网格
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：用 Canvas 手绘异常上下文波形网格、主刻度标签、EtCO2 正常范围带和按真实采样时间定位的 CO2 曲线

## 补充职责

生成纸质报告单风格的 CO2 波形 bitmap，包含密集网格、按上下文窗口动态标注的横轴、横纵轴主刻度标签、纵轴单位、EtCO2 正常范围带和黑色波形线；新记录的曲线点按 `sampleTimeMillis - segment.startMillis` 定位到真实横坐标，旧记录按点序和 `POINTS_PER_SECOND` 兜底。

## 关键 ID / 别名

- 定位别名：manual waveform bitmap, PDF 手绘波形网格
- 关键字段 / 方法：`templateConfig.waveformBitmapWidth`、`templateConfig.waveformBitmapHeight`、`templateConfig.waveformImageMaxHeight`、`ReportSegment.durationMillis`、`waveformXAxisStepSeconds`、`waveformX`、`reportYAxisMax`、`yAxisTickValues`、`drawEtco2ReferenceRange`、`Path`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`templateConfig.waveformBitmapWidth`、`templateConfig.waveformBitmapHeight`、`templateConfig.waveformImageMaxHeight`、`ReportSegment.durationMillis`、`waveformXAxisStepSeconds`、`durationMillisToPoints`、`waveformX`、`reportYAxisMax`、`yAxisTickValues`、`drawEtco2ReferenceRange`、`Path`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.addWaveformChart`。

## 注意事项

波形 bitmap 宽高、PDF 图高、绘图区边界、网格数量、主刻度间隔、文字大小和线宽均来自 `PdfReportTemplateConfig`。`mmHg` 报告纵轴固定显示到 90，并标出 0/30/60/90 mmHg 主刻度；非 `mmHg` 单位沿用当前 CO2 量程并按 0、1/3、2/3、最大值标主刻度。EtCO2 正常范围以 `32-42mmHg` 为基准，按当前 CO2 单位换算后在图内用浅色范围带标出；未知单位不显示范围带。波形横轴按 `ReportSegment.durationMillis` 动态标注，默认异常上下文为 60 秒，也可配置为 `10..300`。有 `sampleTimeMillis` 时按真实采样时间映射 x 坐标，无时间戳的旧数据按点序和 `POINTS_PER_SECOND` 兜底。PDF 波形不再依赖 MPAndroidChart 默认样式。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查网格、坐标和曲线。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
