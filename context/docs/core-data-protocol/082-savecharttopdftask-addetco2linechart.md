# SaveChartToPdfTask.addETCO2LineChart

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L336）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.addETCO2LineChart`
- ID / 别名：add waveform chart, PDF 波形图
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将波形渲染为 Bitmap 后嵌入 PDF

## 补充职责

将过滤后波形绘制为 Bitmap 并嵌入 PDF。

## 关键 ID / 别名

- 定位别名：add waveform chart, PDF 波形图
- 关键字段 / 方法：`filterData(data, maxETCO2)`、`LineChart`、`LineDataSet`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`filterData(data, maxETCO2)`、`LineChart`、`LineDataSet`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`savePDF`。

## 注意事项

Bitmap 固定 1000x340。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
