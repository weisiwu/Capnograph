<!-- context-seed:start -->
# PrintSetting

## 定位

- ID: `AND-PRS`
- 类型: `object`
- 领域: apps
- 来源: `apps/android/hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt:34`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `PrintSetting` 是 Android 端热敏打印和 PDF 输出的配置单例。
- **核心配置**：PDF 模板类型（`PDF_TEMPLATE_OFFICIAL`/`PDF_TEMPLATE_SIMPLE`）、水印默认文字（`DEFAULT_PDF_WATERMARK_TEXT` 为 "WLD Instruments Co., Ltd"）、水印默认透明度（`DEFAULT_PDF_WATERMARK_OPACITY` = 0.1f）、PDF 异常上下文默认秒数（`DEFAULT_PDF_EVENT_CONTEXT_SECONDS` = 3）。
- 所有属性通过 `HotmeltPinter` 类的 `printSetting` 属性访问。
<!-- context-seed:end -->
