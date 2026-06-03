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
- 备注：实时图表、历史图表、PDF 图表和打印 Bitmap 渲染

## 补充职责

折线图渲染库，覆盖实时、历史、PDF 和打印图表。

## 关键 ID / 别名

`com.github.PhilJay:MPAndroidChart:v3.1.0`

## 关键字段 / 方法

`com.github.PhilJay:MPAndroidChart:v3.1.0`；catalog alias `libs.mpandroidchart`。

## 主要调用点

`EtCo2LineChart`、`HistoryRecordDetailActivity`、`PDFKit`、`HotmeltPinter` 使用 `LineChart` 和相关 data/axis API。

## 注意事项

`hotmeltprint/build.gradle.kts` 中 `libs.mpandroidchart` 重复声明 3 次；图表变更需同时验证 UI、PDF、打印 bitmap。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
