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
- 备注：生成临时 PDF，添加固定报告单页眉、完整基础信息、连续 14 秒波形段、单位感知 EtCO2 参考值、签字和可选水印

## 补充职责

生成临时 PDF 并复制成目标 PDF。

## 关键 ID / 别名

- 定位别名：PDF render pipeline, PDF 生成流程
- 关键字段 / 方法：`${filePath}.tmp`、`addPDFHeader`、`addPDFDetail`、`addWaveformSections`、`addPDFFooter`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`${filePath}.tmp`、`Document(PageSize.A4, ...)`、`addPDFHeader`、`addPDFDetail`、`addWaveformSections`、`addPDFFooter`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`onPreExecute`。

## 注意事项

临时文件非空才覆盖输出；失败删除 tmp。报告单样式包含医院名、固定标题 `呼气末二氧化碳监测报告单`、住院号/床位号/科室/姓名/性别/年龄/身高/体重、记录开始时间、结束时间、检测时长、报告生成时间、设备编号、从记录开头连续切分的最多三段 14 秒 CO2 波形、每段测量时间和指标行、按当前 CO2 单位换算的 EtCO2 参考值、签字栏。当前没有身高/体重数据源时显示 `未填写`；参考范围以 `32-42mmHg` 为基准配置，`kPa` 与 `%` 导出时转换到对应单位，未知单位不输出参考行。

## 最小验证方式

检查 `savePDF`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
