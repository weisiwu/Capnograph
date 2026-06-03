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
- 备注：PDF 报告生成；正文 section 分页、全程趋势/异常上下文波形、可配置水印和最后页签字栏由 `PDFKit.kt` 控制

## 补充职责

PDF 生成库，用于纸质报告单样式报告导出，当前通过 `PdfWriter` 读取垂直位置并在 section 空间不足时换页；可配置水印通过 `PdfPageEventHelper` 在页面底层绘制。

## 关键 ID / 别名

`com.itextpdf:itextpdf:5.5.13.4`

## 关键字段 / 方法

`com.itextpdf:itextpdf:5.5.13.4`；两个模块声明。

## 主要调用点

`PDFKit.kt` 使用 `Document`、`PdfWriter`、`PdfPTable`、`PdfPCell`、`PdfPageEventHelper`、`PdfGState` 等生成报告，并嵌入 Canvas 手绘的全程 EtCO2 趋势 bitmap 和异常上下文 CO2 波形 bitmap。基础信息、摘要、趋势图、异常片段和 footer/signature 作为 section 渲染；footer/signature 总是在全部内容之后输出，空间不足时进入最后新页。

## 注意事项

库版本较旧；PDF 改动需验证中文字体、表单字段、section 分页、可配置水印、最后页签字栏、手绘趋势/波形图片和 SAF 导出。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`；手动导出 PDF

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
