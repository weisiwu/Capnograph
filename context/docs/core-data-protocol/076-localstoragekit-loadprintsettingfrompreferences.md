# LocalStorageKit.loadPrintSettingFromPreferences

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L330）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.loadPrintSettingFromPreferences`
- ID / 别名：load print prefs, 读取打印偏好
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：读取 SharedPreferences 并写入共享 `PrintSetting`，包含 PDF 模板/水印、异常波形上下文秒数和详情趋势图偏好

## 补充职责

读取偏好并写入全局 `PrintSetting`，包括 PDF 模板模式、可选水印配置、异常波形上下文秒数和详情趋势图选项。

## 关键 ID / 别名

- 定位别名：load print prefs, 读取打印偏好
- 关键字段 / 方法：`PrintSetting.*`、`KEY_PDF_TEMPLATE_MODE`、`KEY_PDF_WATERMARK_ENABLED`、`KEY_PDF_WATERMARK_TEXT`、`KEY_PDF_WATERMARK_OPACITY`、`KEY_PDF_EVENT_CONTEXT_SECONDS`、`KEY_SHOW_TREND_CHART`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`PrintSetting.*`、`PDF_TEMPLATE_OFFICIAL`、`DEFAULT_PDF_WATERMARK_OPACITY`、`DEFAULT_PDF_EVENT_CONTEXT_SECONDS`、`KEY_PDF_TEMPLATE_MODE`、`KEY_PDF_WATERMARK_ENABLED`、`KEY_PDF_WATERMARK_TEXT`、`KEY_PDF_WATERMARK_OPACITY`、`KEY_PDF_EVENT_CONTEXT_SECONDS`、`KEY_SHOW_TREND_CHART`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

主页初始化、详情导出/打印。

## 注意事项

`age` 默认 0；`showTrendingChart` 默认 true。`pdfTemplateMode` 默认 `PDF_TEMPLATE_OFFICIAL`；未保存水印开关或透明度时对应字段保持 null，PDF 渲染回退到模板默认。透明度读取后限制在 0-1；异常上下文秒数读取后限制在 `10..300`，未保存时保持 null 并回退到模板默认 60 秒。

## 最小验证方式

`rg "loadPrintSettingFromPreferences"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
