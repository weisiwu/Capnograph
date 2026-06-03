# SaveChartToPdfTask.configurePdfChart

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L335）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.configurePdfChart`
- ID / 别名：configure PDF chart, PDF 图表配置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：统一 PDF 中 MPAndroidChart 的轴、标签和边距

## 补充职责

统一 PDF 图表轴、标签、边距。

## 关键 ID / 别名

- 定位别名：configure PDF chart, PDF 图表配置
- 关键字段 / 方法：`axisLeft.axisMaximum`、`xAxisPointStep`、`formatTimeAxisLabel`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`axisLeft.axisMaximum`、`xAxisPointStep`、`formatTimeAxisLabel`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

波形图/趋势图渲染。

## 注意事项

可控制是否绘制 X 轴时间标签。

## 最小验证方式

检查调用参数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
