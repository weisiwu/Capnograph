# MPAndroidChart

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L461）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/056-mpandroidchart.md`。

## 实体定位

- 实体：MPAndroidChart
- ID / 别名：`com.github.PhilJay:MPAndroidChart:v3.1.0`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：实时图表、历史图表和打印 Bitmap 渲染；PDF 报告单波形由 Canvas 手绘

## 补充职责

折线图渲染库，覆盖实时、历史和打印图表；PDF 报告单波形由 `PDFKit` 用 Android Canvas 手绘，不再使用 MPAndroidChart 默认样式。

## 关键 ID / 别名

`com.github.PhilJay:MPAndroidChart:v3.1.0`

## 关键字段 / 方法

`com.github.PhilJay:MPAndroidChart:v3.1.0`；catalog alias `libs.mpandroidchart`。

## 主要调用点

`EtCo2LineChart`、`HistoryRecordDetailActivity`、`HotmeltPinter` 使用 `LineChart` 和相关 data/axis API。`PDFKit` 仍接收 `LineChart` 作为任务入口参数，但报告单波形 bitmap 已改为 Canvas 手绘。

## 注意事项

`hotmeltprint/build.gradle.kts` 中 `libs.mpandroidchart` 重复声明 3 次；图表变更需验证 UI 和打印 bitmap，PDF 模板变更需单独导出报告单验证。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
