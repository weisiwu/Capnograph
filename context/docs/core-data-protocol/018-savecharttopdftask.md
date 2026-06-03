# SaveChartToPdfTask

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L228）。
领域：PDF。
聚合章节：Kit 与服务。

## 实体定位

- 实体：SaveChartToPdfTask
- ID / 别名：chart to PDF task, 图表导出 PDF 任务
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：PDF 导出任务，按模板/打印设置解析水印和异常上下文秒数

## 补充职责

异步 PDF 任务，主线程渲染趋势/波形 bitmap，后台等待结果回调；报告基础信息区会使用传入的记录、打印设置和可选设备序列号，页脚 EtCO2 参考值按 `co2Unit` 换算，水印配置和异常上下文秒数由 `PrintSetting` 覆盖正式/调试模板默认值，并通过 section 渲染入口在内容不足一页时自动换页。

## 关键 ID / 别名

- 定位别名：chart to PDF task, 图表导出 PDF 任务
- 关键字段 / 方法：`onPreExecute`、`doInBackground`、`onPostExecute`、`savePDF`、`WatermarkPageEvent`、`resolveWatermarkConfig`、`resolveEventContextSeconds`、`addTrendSection`、`addReportSection`、`ensurePageSpace`、`co2Unit`、`deviceSerial`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`onPreExecute`、`doInBackground`、`onPostExecute`、`savePDF`、`WatermarkPageEvent`、`resolveWatermarkConfig`、`resolveEventContextSeconds`、`addTrendSection`、`addReportSection`、`ensurePageSpace`、`co2Unit`、`deviceSerial`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`saveChartToPdfInBackground`。

## 注意事项

`AsyncTask` 已过时但当前代码仍使用；失败时回调 false。PDF 内容按基础信息、全程摘要、全程趋势、异常上下文波形、页脚/签字这些 section 渲染，section 渲染前通过 `PdfWriter.getVerticalPosition(true)` 估算剩余空间，不足则 `document.newPage()`。启用水印时设置 `WatermarkPageEvent`，在每页结束时向 `directContentUnder` 重复绘制文字水印。

## 最小验证方式

检查 `PDFKit.kt`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
