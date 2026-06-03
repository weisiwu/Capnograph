# iTextPDF

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L466）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/061-itextpdf.md`。

## 实体定位

- 实体：iTextPDF
- ID / 别名：`com.itextpdf:itextpdf:5.5.13.4`
- 源文件：`app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：PDF 报告生成

## 补充职责

PDF 生成库，用于报告导出。

## 关键 ID / 别名

`com.itextpdf:itextpdf:5.5.13.4`

## 关键字段 / 方法

`com.itextpdf:itextpdf:5.5.13.4`；两个模块声明。

## 主要调用点

`PDFKit.kt` 使用 `Document`、`PdfWriter`、`PdfPTable`、`PdfPCell`、`PdfGState` 等生成报告。

## 注意事项

库版本较旧；PDF 改动需验证中文字体、表格、图表图片和 SAF 导出。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`；手动导出 PDF

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
