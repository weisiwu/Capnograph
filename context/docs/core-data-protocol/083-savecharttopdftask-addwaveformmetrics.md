# SaveChartToPdfTask.addWaveformMetrics

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L340）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.addWaveformMetrics`
- ID / 别名：waveform metrics row, PDF 波形指标行
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：每段波形下输出 EtCO2、FiCO2、RR 的均值/最大/最小/段末统计

## 补充职责

在每段波形下方输出 EtCO2、FiCO2、RR 的分段统计，格式为均值/最大/最小/段末；当前模型没有 SpO2/PR 字段，因此不显示血氧和脉率占位。

## 关键 ID / 别名

- 定位别名：waveform metrics row, PDF 波形指标行
- 关键字段 / 方法：`buildMetrics`、`metricStats`、`formatMetricStats`、`EtCO2`、`FiCO2`、`RR`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`buildMetrics`、`metricStats`、`formatMetricStats`、`EtCO2`、`FiCO2`、`RR`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.addWaveformSections`。

## 注意事项

统计值只接收有限数值；RR 过滤掉 `<= 0` 的无效值。当前历史波形模型没有 SpO2/PR 字段，因此 PDF 指标行不显示血氧和脉率；未来接入血氧模块后，应先从数据模型补字段再显示。RR 单位应为 `bpm`。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查指标行。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
