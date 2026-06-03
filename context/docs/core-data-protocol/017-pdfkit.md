# PDFKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L227）。
领域：PDF。
聚合章节：Kit 与服务。

## 实体定位

- 实体：PDFKit
- ID / 别名：PDF, 报告导出
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：iTextPDF 报告生成和图表导出

## 补充职责

PDF 生成与波形过滤工具。

## 关键 ID / 别名

- 定位别名：PDF, 报告导出
- 关键字段 / 方法：`filterData`、`saveChartToPdfInBackground`、`SaveChartToPdfTask`、`fontPath`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`filterData`、`saveChartToPdfInBackground`、`SaveChartToPdfTask`、`fontPath`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

详情页导出、`LocalStorageKit.saveRecord` 预生成尝试。

## 注意事项

`baseFont` 在文件加载时创建，依赖 SimSun 字体资产。

## 最小验证方式

`rg "fontPath|SaveChartToPdfTask"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
