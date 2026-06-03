# CO2WavePointData

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L134）。
领域：数据模型。

## 实体定位

- 实体：CO2WavePointData
- ID / 别名：waveform point, CO2数据点, 波形点
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：字段：`co2`、`RR`、`ETCO2`、`FiCO2`、`sampleTimeMillis`、`index`

## 补充职责

记录/渲染波形的 Serializable 数据点，字段为 co2、RR、ETCO2、FiCO2、sampleTimeMillis、index；PDF 报告的 15 秒分段统计使用 ETCO2、FiCO2、RR，波形分段和横轴优先使用 sampleTimeMillis。

## 关键 ID / 别名

waveform point, CO2数据点, 波形点

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`

## 主要调用点

按源文件和定位表反查调用点。

## 注意事项

字段：`co2`、`RR`、`ETCO2`、`FiCO2`、`sampleTimeMillis`、`index`。`sampleTimeMillis` 为采样到达时的 epoch millis，新记录会随 GZIP JSON 一起保存；旧记录缺失该字段时值为 0，PDF 回退到 `record.startTime + index / POINTS_PER_SECOND`。当前没有 SpO2/PR 历史字段，PDF 报告不显示 SpO2/PR；未来接入血氧模块后，应先在数据模型补真实字段再显示。

## 最小验证方式

./gradlew :app:assembleDebug

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
