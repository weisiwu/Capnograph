# LocalStorageKit.saveUserPrintSettingToPreferences

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L329）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.saveUserPrintSettingToPreferences`
- ID / 别名：save print prefs, 保存打印偏好
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：保存医院名、报告名、患者信息、输出类型、PDF 模板/水印配置和趋势图开关

## 补充职责

保存打印/PDF/患者偏好，包括 PDF 模板模式和可选水印配置。

## 关键 ID / 别名

- 定位别名：save print prefs, 保存打印偏好
- 关键字段 / 方法：`hospital_name`、`report_name`、`is_output_pdf`、`pdf_template_mode`、`pdf_watermark_enabled`、`pdf_watermark_text`、`pdf_watermark_opacity`、`show_trend_chart` 等。

## 关键字段 / 方法

- 主要字段、方法或协议值：`hospital_name`、`report_name`、`is_output_pdf`、`pdf_template_mode`、`pdf_watermark_enabled`、`pdf_watermark_text`、`pdf_watermark_opacity`、`show_trend_chart` 等。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

主页开始/停止记录前、打印设置页。

## 注意事项

可选字段为 null 时不覆盖旧值；`isPDF` 总是写入。`pdfWatermarkOpacity` 保存前限制在 0-1。

## 最小验证方式

检查 prefs key

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
