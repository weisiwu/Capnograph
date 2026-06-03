# AppStateModel.updatePdfWatermarkOpacity

来源批次：PDF 模板状态补充。
定位入口：`context/entity-id-mapping.md`。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updatePdfWatermarkOpacity`
- ID / 别名：PDF watermark opacity, PDF水印透明度
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：更新 PDF 水印透明度并限制在 0-1

## 补充职责

更新 AppState 中的 PDF 水印透明度。默认值来自 `PrintSetting.DEFAULT_PDF_WATERMARK_OPACITY`。

## 关键 ID / 别名

- 定位别名：PDF watermark opacity, PDF水印透明度
- 关键字段 / 方法：`pdfWatermarkOpacity`、`updatePdfWatermarkOpacity`、`DEFAULT_PDF_WATERMARK_OPACITY`

## 关键字段 / 方法

- 主要字段、方法或协议值：`pdfWatermarkOpacity`、`updatePdfWatermarkOpacity`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`

## 主要调用点

PDF/打印设置相关 UI 或本地偏好回读入口。

## 注意事项

函数内部使用 `coerceIn(0f, 1f)`，持久化保存/读取也会限制到 0-1。

## 最小验证方式

`rg "updatePdfWatermarkOpacity|pdfWatermarkOpacity"`；`./gradlew :app:compileDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
