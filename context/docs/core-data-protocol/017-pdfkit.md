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

PDF 生成与波形过滤工具；PDF 导出使用固定标题的纸质报告单样式，基础信息区输出住院号、床位号、科室、患者信息、起止时间、检测时长、报告时间和设备编号，并从记录开头按 14 秒窗口手绘 CO2 波形网格和指标行，页脚 EtCO2 参考值按当前 CO2 单位换算。

## 关键 ID / 别名

- 定位别名：PDF, 报告导出
- 关键字段 / 方法：`filterData`、`saveChartToPdfInBackground`、`SaveChartToPdfTask`、`addWaveformSections`、`createWaveformBitmap`、`fontPath`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`filterData`、`saveChartToPdfInBackground`、`SaveChartToPdfTask`、`addWaveformSections`、`createWaveformBitmap`、`fontPath`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

详情页导出、`LocalStorageKit.saveRecord` 预生成尝试。

## 注意事项

`baseFont` 在文件加载时创建，依赖 SimSun 字体资产。PDF 波形 bitmap 由 Android Canvas 手绘，不再使用 MPAndroidChart 的默认图表样式。报告正文标题固定为 `呼气末二氧化碳监测报告单`，不读取打印设置里的 `reportName`；科室来自 `PrintSetting.pdfDepart`，设备编号由导出入口传入当前设备序列号，缺值时新增字段显示 `未填写`。EtCO2 参考范围以 `32-42mmHg` 为基准配置，`kPa` 与 `%` 导出时转换到对应单位，未知单位不输出参考行。

## 最小验证方式

`rg "fontPath|SaveChartToPdfTask"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
