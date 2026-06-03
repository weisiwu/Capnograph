# com.wldmedical.hotmeltprint` package

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L42）。
领域：模块。
实体级上下文：`context/docs/platform-resources/014-com-wldmedical-hotmeltprint-package.md`。

## 实体定位

- 实体：`com.wldmedical.hotmeltprint` package
- ID / 别名：printer SDK wrapper, 热敏打印包
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：GPrinter SDK 的项目内封装

## 补充职责

热敏打印封装包，核心文件 `HotmeltPinter.kt` 包含 `PrintSetting`、`Printer` 和 `HotmeltPinter`。

## 关键 ID / 别名

printer SDK wrapper, 热敏打印包

## 关键字段 / 方法

关键实体/文件：HotmeltPinter.kt, PrintSetting, Printer, HotmeltPinter。

## 主要调用点

类名 `HotmeltPinter` 为源码现状拼写；依赖 `com.gprinter.*`、MPAndroidChart 和 iTextPDF。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
