# PDF long record report strategy

来源批次：PDF 长记录报告策略补充。
定位入口：`context/entity-id-mapping.md`。
领域：PDF 策略。
聚合章节：PDF 配置。

## 实体定位

- 实体：PDF long record report strategy
- ID / 别名：long PDF record, 长记录报告, 全程趋势, 异常上下文波形
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`
- 原始补充上下文：用户要求选择“方案 1”，异常给 60 秒上下文波形且时间可配置，并要求记录调研信息
- 备注：长时间监测报告采用全程摘要 + 全程 EtCO2 趋势 + 异常上下文波形；不支持固定只取开头 42 秒作为正式报告策略

## 补充职责

记录 PDF 长记录展示策略、实现边界和调研依据。当前报告把长记录拆为三层：全程摘要提供数值概览，全程 EtCO2 趋势覆盖完整时间轴，异常上下文波形提供可读的原始波形证据。

## 关键 ID / 别名

- 定位别名：long PDF record, 长记录报告, 全程趋势, 异常上下文波形
- 关键字段 / 方法：`addReportSummary`、`addTrendSection`、`createTrendBitmap`、`addWaveformSections`、`buildAbnormalReportSegments`、`buildAbnormalEvents`、`buildAbnormalWindows`、`resolveEventContextSeconds`

## 关键字段 / 方法

- 主要字段、方法或协议值：`defaultEventContextSeconds=60`、`MIN_PDF_EVENT_CONTEXT_SECONDS=10`、`MAX_PDF_EVENT_CONTEXT_SECONDS=300`、`trendMaxBuckets=1200`、`abnormalEtco2LowMmHg=25`、`abnormalEtco2HighMmHg=50`、`abnormalRrLow=5`、`abnormalRrHigh=30`
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

`savePDF` 渲染顺序为：页眉、基础信息、全程摘要、全程趋势、异常片段、页脚/签字。`createTrendBitmap` 对全记录 EtCO2 做桶聚合，最多 `1200` 桶，绘制平均线和 min/max 竖向范围线。

`addWaveformSections` 只输出超过报告阈值的异常上下文窗口。当前回退阈值为 EtCO2 `<25` 或 `>50mmHg`、RR `<5` 或 `>30bpm`；EtCO2 阈值会按当前 CO2 单位换算。上下文窗口默认 `60` 秒，通过打印设置 `pdfEventContextSeconds` 配置，保存和读取时裁剪到 `10..300` 秒。窗口会在记录边界内截断，重叠或相近窗口按 `abnormalMergeGapSeconds` 合并。没有异常时，PDF 显示“未检测到超过报告阈值的 EtCO2/RR 异常”并仍输出阈值说明。

## 注意事项

当前数据模型没有保存真实报警事件、报警阈值快照、呼吸状态或 SpO2/PR 字段；PDF 暂时从已存 `CO2WavePointData.ETCO2` 和 `RR` 重新计算异常。后续如果记录模型补充真实事件流或报警配置快照，应优先使用事件流和快照阈值，而不是继续依赖报告端回算。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`；手动导出包含正常记录和长记录的 PDF，检查摘要、全程 EtCO2 趋势、异常上下文波形、无异常提示和上下文秒数配置。

## 同步要求

- 如果 PDF 长记录展示策略、异常阈值、上下文秒数范围、调研结论或源文件发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
