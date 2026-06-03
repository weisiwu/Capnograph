# English strings

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L481）。
领域：资源。
实体级上下文：`context/docs/platform-resources/071-english-strings.md`。

## 实体定位

- 实体：English strings
- ID / 别名：`values-en/strings.xml`, 英文文案
- 源文件：`app/src/main/res/values-en/strings.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：英文本地化字符串；包含 PDF 模板、水印和异常波形上下文设置文案

## 补充职责

英文本地化字符串资源，包含 PDF 模板、水印开关、水印文字、水印透明度和异常波形上下文秒数输入提示。

## 关键 ID / 别名

`values-en/strings.xml`, 英文文案

## 关键字段 / 方法

`values-en/strings.xml`；支持 `en` locale；多数 key 与中文默认资源对齐；PDF 模板/水印/异常上下文 key 与中文默认资源保持同名。

## 主要调用点

AppCompat 语言切换和系统 locale 选择英文文案。

## 注意事项

`pdf_patient_age_unit` 与 `etco2table_age_unit` 当前为空字符串，和中文的“岁”不同。

## 最小验证方式

`./gradlew :app:assembleDebug`；手动切换英文检查文案

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
