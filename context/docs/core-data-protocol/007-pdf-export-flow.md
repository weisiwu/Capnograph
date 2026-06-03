# PDF export flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L115）。
领域：PDF。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：PDF export flow
- ID / 别名：save PDF, PDF 报告导出
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`, `app/src/main/assets/fonts/SimSun.ttf`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：记录详情生成纸质报告单样式 PDF、写 cache，再通过系统创建文档保存

## 补充职责

历史详情页读取全量 CO2Data，生成纸质报告单样式 cache PDF，再通过系统文档创建器保存。

## 关键 ID / 别名

- 定位别名：save PDF, PDF 报告导出
- 关键字段 / 方法：`HistoryRecordDetailActivity.onSavePDFClick`、`loadAllCo2Data`、`saveChartToPdfInBackground`、`SaveChartToPdfTask.savePDF`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`HistoryRecordDetailActivity.onSavePDFClick`、`loadAllCo2Data`、`saveChartToPdfInBackground`、`SaveChartToPdfTask.savePDF`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`, `app/src/main/assets/fonts/SimSun.ttf`

## 主要调用点

详情页右上角；`viewModel.isPDF == true`。

## 注意事项

`SaveChartToPdfTask` 用 `AsyncTask` + 主线程生成 Canvas 波形 bitmap；PDF 版式参考纸质报告单，标题固定为 `呼气末二氧化碳监测报告单`，包含住院号、床位号、科室、姓名、性别、年龄、身高/体重占位、记录开始/结束时间、检测时长、报告生成时间、设备编号、从记录开头连续切分的最多三段 14 秒波形、指标行、按当前 CO2 单位换算的 EtCO2 参考值和签字栏；字体为 `assets/fonts/SimSun.ttf`。

## 最小验证方式

`rg "ACTION_CREATE_DOCUMENT|saveChartToPdfInBackground"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
