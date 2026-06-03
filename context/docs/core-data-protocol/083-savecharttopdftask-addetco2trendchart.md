# SaveChartToPdfTask.addETCO2TrendChart

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L337）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.addETCO2TrendChart`
- ID / 别名：add trend chart, PDF 趋势图
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：按间隔生成 ETCO2 趋势图并嵌入 PDF

## 补充职责

按间隔绘制 ETCO2 趋势图并嵌入 PDF。

## 关键 ID / 别名

- 定位别名：add trend chart, PDF 趋势图
- 关键字段 / 方法：`for (i in data.indices step 50)`、`xAxisPointStep = 50f`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`for (i in data.indices step 50)`、`xAxisPointStep = 50f`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`savePDF` 且 `showTrendingChart` 为 true。

## 注意事项

Bitmap 固定 1000x260。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
