# AppStateModel.updatePdfWatermarkText

来源批次：PDF 模板状态补充。
定位入口：`context/entity-id-mapping.md`。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updatePdfWatermarkText`
- ID / 别名：PDF watermark text, PDF水印文字
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：更新 PDF 水印文字，默认万联达仪器

## 补充职责

更新 AppState 中的 PDF 水印文字。默认值来自 `PrintSetting.DEFAULT_PDF_WATERMARK_TEXT`。

## 关键 ID / 别名

- 定位别名：PDF watermark text, PDF水印文字
- 关键字段 / 方法：`pdfWatermarkText`、`updatePdfWatermarkText`、`DEFAULT_PDF_WATERMARK_TEXT`

## 关键字段 / 方法

- 主要字段、方法或协议值：`pdfWatermarkText`、`updatePdfWatermarkText`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`

## 主要调用点

PDF/打印设置相关 UI 或本地偏好回读入口。

## 注意事项

当前函数不裁剪空白；调用方如需禁止空水印文字，应在 UI 或保存层处理。

## 最小验证方式

`rg "updatePdfWatermarkText|pdfWatermarkText"`；`./gradlew :app:compileDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
