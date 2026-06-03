# PDFKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L227）。
领域：PDF。
聚合章节：Kit 与服务。

## 实体定位

- 实体：PDFKit
- ID / 别名：PDF, 报告导出
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：iTextPDF 报告生成和图表导出；长记录报告输出全程趋势和异常上下文波形

## 补充职责

PDF 生成与波形过滤工具；PDF 导出使用 `PdfReportTemplateConfig` 管理标题、页边距、字体、表格列宽、section 高度、趋势图尺寸、波形尺寸、异常阈值和模板默认水印。导出内容使用纸质报告单样式，基础信息区输出住院号、床位号、科室、患者信息、起止时间、检测时长、报告时间和设备编号；基础信息后输出全程摘要和全程 EtCO2 趋势，再按 EtCO2/RR 异常事件输出可配置秒数的上下文 CO2 波形和 EtCO2/FiCO2/RR 分段统计，页脚 EtCO2 参考值按当前 CO2 单位换算。PDF section 渲染器会在基础信息、摘要、趋势图、异常片段和 footer/signature 前检查剩余空间，不足时自动分页。

## 关键 ID / 别名

- 定位别名：PDF, 报告导出
- 关键字段 / 方法：`PdfReportTemplateConfig`、`PdfWatermarkConfig`、`filterData`、`saveChartToPdfInBackground`、`SaveChartToPdfTask`、`WatermarkPageEvent`、`resolveWatermarkConfig`、`addTrendSection`、`addReportSection`、`ensurePageSpace`、`addWaveformSections`、`createTrendBitmap`、`createWaveformBitmap`、`fontPath`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`PdfReportTemplateConfig`、`PDF_OFFICIAL_REPORT_TEMPLATE_CONFIG`、`PDF_DEBUG_REPORT_TEMPLATE_CONFIG`、`PdfWatermarkConfig`、`filterData`、`saveChartToPdfInBackground`、`SaveChartToPdfTask`、`WatermarkPageEvent`、`resolveWatermarkConfig`、`addTrendSection`、`addReportSection`、`ensurePageSpace`、`addWaveformSections`、`createTrendBitmap`、`createWaveformBitmap`、`fontPath`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

详情页导出、`LocalStorageKit.saveRecord` 预生成尝试。

## 注意事项

`baseFont` 在文件加载时创建，依赖 SimSun 字体资产。PDF 趋势图和波形 bitmap 由 Android Canvas 手绘，不再使用 MPAndroidChart 的默认图表样式；`mmHg` 纵轴标 0/30/60/90 主刻度，非 `mmHg` 单位按当前量程标 0、1/3、2/3、最大值。报告正文标题默认是 `呼气末二氧化碳监测报告单`，由 `PdfReportTemplateConfig.title` 管理，不读取打印设置里的 `reportName`；科室来自 `PrintSetting.pdfDepart`，设备编号由导出入口传入当前设备序列号，缺值时新增字段显示 `未填写`。水印由 `PrintSetting` 优先控制，缺省时回退到正式/调试模板默认值；启用水印时通过 `PdfPageEventHelper` 在每页底层重复绘制，文字和透明度可配置。长记录报告默认输出全程 EtCO2 趋势；异常片段由 EtCO2/RR 阈值判定，默认 EtCO2 <25 或 >50mmHg、RR <5 或 >30bpm，EtCO2 阈值会随 `co2Unit` 换算；异常上下文波形默认 60 秒，`PrintSetting.pdfEventContextSeconds` 可覆盖并限制在 10-300 秒。EtCO2 参考范围以 `32-42mmHg` 为基准配置，`kPa` 与 `%` 导出时转换到对应单位，未知单位不输出参考行或图内范围带。全程摘要和每段指标行均显示均值/最大/最小/段末，避免只依赖瞬时末值；当前没有 SpO2/PR 数据模型字段，PDF 不输出血氧和脉率占位。Footer/signature 总是在所有内容后渲染，并在空间不足时换到最后新页。调版优先改 `PdfReportTemplateConfig` 默认值。

## 最小验证方式

`rg "fontPath|SaveChartToPdfTask"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
