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

`SaveChartToPdfTask` 用 `AsyncTask` + 主线程生成 Canvas 波形 bitmap；PDF 版式参考纸质报告单，标题、页边距、字体、表格列宽、section 高度、波形尺寸、分段参数和模板默认水印由 `PdfReportTemplateConfig` 集中管理。默认标题为 `呼气末二氧化碳监测报告单`，内容包含住院号、床位号、科室、姓名、性别、年龄、身高/体重占位、记录开始/结束时间、检测时长、报告生成时间、设备编号、全程摘要、从记录开头连续切分的最多三段 14 秒波形、EtCO2/FiCO2/RR 分段统计、按当前 CO2 单位换算的 EtCO2 参考值和签字栏；字体为 `assets/fonts/SimSun.ttf`。正式报告模板默认关闭水印，调试报告模板默认启用水印；`PrintSetting` 可覆盖水印开关、文字和透明度，启用后通过 iText page event 在每页底层绘制。新记录的波形段和横坐标优先使用 `CO2WavePointData.sampleTimeMillis`，旧记录回退到 `record.startTime + index / POINTS_PER_SECOND`。波形图内显示横纵轴主刻度标签，并按当前 CO2 单位把 EtCO2 正常范围绘制为浅色范围带。全程摘要和分段统计均输出均值/最大/最小/段末，当前没有 SpO2/PR 数据模型字段，PDF 不输出血氧和脉率占位。基础信息、摘要、每段波形和 footer/signature 通过 section 渲染器检查剩余页面空间，不足时分页；footer/signature 始终在全部内容之后出现在最后一页。

## 最小验证方式

`rg "ACTION_CREATE_DOCUMENT|saveChartToPdfInBackground"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
