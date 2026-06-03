# AppStateModel.updatePdfTemplateMode

来源批次：PDF 模板状态补充。
定位入口：`context/entity-id-mapping.md`。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updatePdfTemplateMode`
- ID / 别名：PDF template mode, PDF模板模式
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：更新 PDF 报告模板模式，默认 official

## 补充职责

更新 AppState 中的 PDF 模板模式。默认值来自 `PrintSetting.PDF_TEMPLATE_OFFICIAL`，可选 debug 值由 `PrintSetting.PDF_TEMPLATE_DEBUG` 定义。

## 关键 ID / 别名

- 定位别名：PDF template mode, PDF模板模式
- 关键字段 / 方法：`pdfTemplateMode`、`updatePdfTemplateMode`、`PDF_TEMPLATE_OFFICIAL`、`PDF_TEMPLATE_DEBUG`

## 关键字段 / 方法

- 主要字段、方法或协议值：`pdfTemplateMode`、`updatePdfTemplateMode`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`

## 主要调用点

PDF/打印设置相关 UI 或本地偏好回读入口。

## 注意事项

当前函数只写入状态，不校验字符串枚举；调用方应传入 `PrintSetting` 中定义的模板模式。

## 最小验证方式

`rg "updatePdfTemplateMode|pdfTemplateMode"`；`./gradlew :app:compileDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
