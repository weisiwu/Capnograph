# SaveChartToPdfTask.savePDF

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L334）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.savePDF`
- ID / 别名：PDF render pipeline, PDF 生成流程
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：生成临时 PDF，按 section 分页添加模板化报告单页眉、表单基础信息、连续 15 秒波形段、分段统计、单位感知 EtCO2 参考值、签字和可选水印

## 补充职责

生成临时 PDF 并复制成目标 PDF。

## 关键 ID / 别名

- 定位别名：PDF render pipeline, PDF 生成流程
- 关键字段 / 方法：`${filePath}.tmp`、`resolveWatermarkConfig`、`WatermarkPageEvent`、`addPDFHeader`、`addReportSection`、`ensurePageSpace`、`addPDFDetail`、`addWaveformSections`、`addPDFFooter`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`${filePath}.tmp`、`Document(templateConfig.pageSize, ...)`、`templateConfig.pageMargin*`、`PdfWriter.getVerticalPosition(true)`、`resolveWatermarkConfig`、`WatermarkPageEvent`、`addPDFHeader`、`addReportSection`、`ensurePageSpace`、`addPDFDetail`、`addWaveformSections`、`addPDFFooter`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`onPreExecute`。

## 注意事项

临时文件非空才覆盖输出；失败删除 tmp。页面尺寸、页边距、默认标题、字体大小、基础信息表格列宽、section 估算高度、15 秒波形尺寸和模板默认水印均来自 `PdfReportTemplateConfig`。`savePDF` 调用 `resolveWatermarkConfig`：`PrintSetting` 有保存值时优先使用，否则回退到正式/调试模板默认；正式模板默认无水印，调试模板默认用 `万联达仪器`、透明度 `0.3`。启用水印时在 `document.open()` 前设置 `WatermarkPageEvent`，每页结束时在底层重复绘制文字。报告单样式包含医院名、默认标题 `呼气末二氧化碳监测报告单`、住院号/床位号/科室/姓名/性别、连续 15 秒 CO2 波形、每段测量时间和分段统计、按当前 CO2 单位换算的 EtCO2 参考值、签字栏。基础信息不展示开始时间、结束时间、报告时间、检测时长、设备编号、身高、体重、年龄；标签统一带冒号并右对齐到冒号，值列使用等长下划线，空值只保留下划线。分段统计显示 EtCO2/FiCO2/RR 的均值/最大/最小/段末；参考范围以 `32-42mmHg` 为基准配置，`kPa` 与 `%` 导出时转换到对应单位，未知单位不输出参考行或图内范围带。当前不输出全程摘要、全程趋势或异常上下文波形，`PrintSetting.pdfEventContextSeconds` 不参与导出。基础信息、每个 15 秒波形段和 footer/signature 按配置的 section 估算高度，渲染前检查剩余空间，不足时 `document.newPage()`；footer/signature 在所有内容之后渲染，空间不足时固定到最后新页。新记录测量时间和横轴优先使用 `sampleTimeMillis`，旧记录回退到固定采样率。

## 最小验证方式

检查 `savePDF`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
