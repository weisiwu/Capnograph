# PdfReportTemplateConfig

来源批次：PDF 报告模板配置补充。
定位入口：`context/entity-id-mapping.md`。
领域：PDF 配置。
聚合章节：PDF 配置。

## 实体定位

- 实体：PdfReportTemplateConfig
- ID / 别名：PDF template config, PDF 报告模板配置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：集中管理 PDF 报告标题、页边距、字体、表格列宽、section 高度、15 秒波形尺寸和模板默认水印

## 补充职责

PDF 报告模板配置对象，避免 A4 页面、页边距、字体大小、表格列宽、15 秒波形 bitmap 尺寸、图高和模板默认水印参数散落在渲染逻辑中；正式报告模板默认不加水印，调试报告模板默认启用水印。

## 关键 ID / 别名

- 定位别名：PDF template config, PDF 报告模板配置
- 关键字段 / 方法：`title`、`pageSize`、`pageMargin*`、`titleFontSize`、`detailColumnWidths`、`detailFieldColumnWidths`、`fieldLabelPaddingRight`、`fieldValueBorderWidth`、`waveformSectionHeight`、`waveformSegmentDurationSeconds`、`waveformBitmapWidth`、`waveformBitmapHeight`、`defaultWatermarkEnabled`、`defaultWatermarkText`、`defaultWatermarkOpacity`、`watermarkFontSize`、`watermarkRotation`

## 关键字段 / 方法

- 主要字段、方法或协议值：`PdfReportTemplateConfig`、`PDF_OFFICIAL_REPORT_TEMPLATE_CONFIG`、`PDF_DEBUG_REPORT_TEMPLATE_CONFIG`、`pdfReportTemplateConfigFor`、`templateConfig`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask` 的页眉、基础信息、连续 15 秒波形段、页脚、分页预估、`Document` 初始化和水印默认值。

## 注意事项

调版优先改 `PdfReportTemplateConfig` 默认值。基础信息区由 `detailColumnWidths` 管理外层两列，即每行展示两个属性；由 `detailFieldColumnWidths`、`fieldLabelPaddingRight`、`fieldValuePaddingBottom`、`fieldValueBorderWidth` 管理每个字段内部的标签右对齐、等长下划线和值列边框。长记录报告策略由代码实现为按完整记录时间轴输出连续 15 秒波形段；模板配置波形版式、横轴主刻度和每段秒数。`PrintSetting.pdfTemplateMode` 选择正式或调试模板；`pdfWatermarkEnabled`、`pdfWatermarkText`、`pdfWatermarkOpacity` 有保存值时覆盖模板默认水印。`pdfEventContextSeconds` 当前不参与 PDF 导出。EtCO2 参考范围和单位换算仍是业务配置，不属于模板版式配置。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查页边距、表格、分页和波形图尺寸。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
