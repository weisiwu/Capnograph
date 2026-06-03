# PrintSetting

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L238）。
领域：打印。
聚合章节：Kit 与服务。

## 实体定位

- 实体：PrintSetting
- ID / 别名：print settings, 打印设置
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：共享打印设置对象；PDF 报告基础信息读取患者信息，并保留 PDF 模板/水印和异常上下文秒数偏好

## 补充职责

PDF/小票共享打印配置；PDF 报告基础信息区会读取 `pdfDepart`、`pdfBedNumber`、`pdfIDNumber`、`name`、`gender`、`age`。PDF 模板模式、水印开关/文字/透明度和异常片段上下文秒数也保存在该全局对象中，供后续 PDF 模板渲染使用。

## 关键 ID / 别名

- 定位别名：print settings, 打印设置
- 关键字段 / 方法：`hospitalName`、`reportName`、`isPDF`、`pdfTemplateMode`、`pdfWatermarkEnabled`、`pdfWatermarkText`、`pdfWatermarkOpacity`、`pdfEventContextSeconds`、`pdfDepart`、`pdfBedNumber`、`pdfIDNumber`、`name`、`gender`、`age`、`showTrendingChart`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`PDF_TEMPLATE_OFFICIAL`、`PDF_TEMPLATE_DEBUG`、`DEFAULT_PDF_WATERMARK_TEXT`、`DEFAULT_PDF_WATERMARK_OPACITY`、`DEFAULT_PDF_EVENT_CONTEXT_SECONDS`、`MIN_PDF_EVENT_CONTEXT_SECONDS`、`MAX_PDF_EVENT_CONTEXT_SECONDS`、`hospitalName`、`reportName`、`isPDF`、`pdfTemplateMode`、`pdfWatermarkEnabled`、`pdfWatermarkText`、`pdfWatermarkOpacity`、`pdfEventContextSeconds`、`pdfDepart`、`pdfBedNumber`、`pdfIDNumber`、`name`、`gender`、`age`、`showTrendingChart`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`LocalStorageKit.loadPrintSettingFromPreferences`、PDF/打印输出。

## 注意事项

全局 object，可被偏好读取覆盖。PDF 报告科室字段来自 `pdfDepart`，缺值时报告显示 `未填写`。`pdfTemplateMode` 默认为正式报告；`pdfWatermarkEnabled` 为 null 时 PDF 使用模板默认值，正式模板默认关闭水印，调试模板默认启用水印。水印文字默认 `万联达仪器`，透明度默认 `0.3` 且读取/保存时限制在 `0..1`。`pdfEventContextSeconds` 为异常片段上下文总秒数，未保存时使用默认 60 秒，读取/保存和 ViewModel 更新时限制在 10-300 秒。

## 最小验证方式

`rg "PrintSetting\\."`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
