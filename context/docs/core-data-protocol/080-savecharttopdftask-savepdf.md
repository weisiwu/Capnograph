# SaveChartToPdfTask.savePDF

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L334）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.savePDF`
- ID / 别名：PDF render pipeline, PDF 生成流程
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：生成临时 PDF，添加页眉、基础信息、ETCO2/RR、趋势图、波形图、页脚和可选水印

## 补充职责

生成临时 PDF 并复制成目标 PDF。

## 关键 ID / 别名

- 定位别名：PDF render pipeline, PDF 生成流程
- 关键字段 / 方法：`${filePath}.tmp`、`addPDFHeader`、`addPDFDetail`、`addETCO2TrendChart`、`addETCO2LineChart`、`addPDFFooter`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`${filePath}.tmp`、`addPDFHeader`、`addPDFDetail`、`addETCO2TrendChart`、`addETCO2LineChart`、`addPDFFooter`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`onPreExecute`。

## 注意事项

临时文件非空才覆盖输出；失败删除 tmp。

## 最小验证方式

检查 `savePDF`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
