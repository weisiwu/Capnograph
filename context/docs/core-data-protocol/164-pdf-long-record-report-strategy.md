# PDF long record report strategy

来源批次：PDF 长记录报告策略补充。
定位入口：`context/entity-id-mapping.md`。
领域：PDF 策略。
聚合章节：PDF 配置。

## 实体定位

- 实体：PDF long record report strategy
- ID / 别名：long PDF record, 长记录报告, 连续波形, 15秒波形段
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`
- 原始补充上下文：历史上曾按“方案 1”输出异常 60 秒上下文波形并记录调研信息；当前需求已改为连续 15 秒波形段
- 备注：长时间监测报告直接按完整记录时间轴输出连续 15 秒波形段；不再输出全程摘要、全程趋势或异常上下文波形

## 补充职责

记录 PDF 长记录展示策略、实现边界和调研依据。当前报告直接把完整记录时间轴切成连续 15 秒 CO2 波形段，按段输出原始波形和 EtCO2/FiCO2/RR 统计。

## 关键 ID / 别名

- 定位别名：long PDF record, 长记录报告, 连续波形, 15秒波形段
- 关键字段 / 方法：`addWaveformSections`、`buildWaveformSegments`、`createWaveformBitmap`、`addWaveformMetrics`、`waveformSegmentDurationSeconds`

## 关键字段 / 方法

- 主要字段、方法或协议值：`waveformSegmentDurationSeconds=15`、`waveformXAxisLabelStepSeconds=5`、`buildWaveformSegments`、`pointTimelineMillis`、`displayTimelineMillis`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 调研记录

调研时间：2026-06-04。

公开资料没有发现支持“正式报告固定只展示开头 42 秒波形”的模式。相近产品和论文更常见的逻辑是：保存长时间趋势、表格、事件摘要和可导出的原始数据；实时或事件报告才输出短波形条带。

- Medtronic Capnostream 35：公开产品资料强调长时间趋势/数据存储、图形趋势窗口和报告类型。链接：`https://www.medtronic.com/en-us/healthcare-professionals/products/patient-monitoring/capnography-monitoring/portable-respiratory-monitors/capnostream-35-portable-respiratory-monitor.html`
- Capnostream 20/20p 操作资料：包含 trend/case/realtime 等报告类型，实时报告可包含波形条带，历史数据支持下载/导出。链接：`https://usme.com/wp-content/uploads/2022/06/Oridion-Capnostream20-Operator-manual-Nellcor.pdf`，`https://manualzz.com/doc/o/1sb4uj/oridion-capnostream-20p-operator-s-manual-downloading-patient-data`
- Masimo Trace：公开资料描述 configurable reports、parameter/event reports、comprehensive report，覆盖趋势、事件、高低均值等摘要。链接：`https://www.masimo.com/ca-fr/products/analytics-and-reporting/trace/`，`https://techdocs.masimo.com/globalassets/techdocs/pdf/lab-9341c_master.pdf`
- Capnography waveform sequence analysis：论文按每秒状态分析全程波形序列，体现长记录应覆盖完整时间轴，而不是只展示开头。链接：`https://pmc.ncbi.nlm.nih.gov/articles/PMC6629622/`
- 连续呼吸抑制/PRODIGY 与机器学习相关研究：常用连续监测、事件窗口和趋势/特征提取思路。链接：`https://pmc.ncbi.nlm.nih.gov/articles/PMC7467153/`，`https://link.springer.com/article/10.1007/s10877-024-01155-0`
- 围术期监测建议/记录规范：持续监测与间隔记录并存，报告重点是全程状态和事件记录。链接：`https://pmc.ncbi.nlm.nih.gov/articles/PMC7222867/`，`https://www.ovid.com/journals/anes/pdf/10.1111/anae.15501~recommendations-for-standards-of-monitoring-during`

## 当前实现

`savePDF` 渲染顺序为：页眉、基础信息、连续 15 秒波形段、页脚/签字。`addWaveformSections` 不再按 EtCO2/RR 阈值筛选异常，也不输出无异常提示、异常原因或异常时间。

`buildWaveformSegments` 优先按 `CO2WavePointData.sampleTimeMillis` 切分真实采样时间轴；旧记录没有采样时间时回退到 `index / POINTS_PER_SECOND`。每段固定 15 秒，横轴标签默认 0/5/10/15 秒。`PrintSetting.pdfEventContextSeconds` 仍可保存和加载，但当前 PDF 导出不再读取该值。

## 注意事项

当前数据模型没有保存 SpO2/PR 字段；PDF 分段统计仍只输出 EtCO2、FiCO2 和 RR。后续如果重新引入事件报告，应优先使用真实事件流和快照阈值，而不是在报告端临时回算异常。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`；手动导出长记录 PDF，检查完整记录按 15 秒连续切段、页脚签字和分段统计。

## 同步要求

- 如果 PDF 长记录展示策略、波形段秒数、调研结论或源文件发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
