# saveChartToPdfInBackground

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L333）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`saveChartToPdfInBackground`
- ID / 别名：PDF task entry, PDF 任务入口
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：按记录时间顺序复制数据、重排 index，并携带可选设备序列号启动 SaveChartToPdfTask

## 补充职责

按记录时间顺序复制数据、重排 index，携带打印设置、记录、CO2 单位和可选设备序列号启动 PDF 任务。

## 关键 ID / 别名

- 定位别名：PDF task entry, PDF 任务入口
- 关键字段 / 方法：`mapIndexed { item.copy(index = index) }`、`co2Unit`、`deviceSerial`、`SaveChartToPdfTask.execute()`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`mapIndexed { item.copy(index = index) }`、`co2Unit`、`deviceSerial`、`SaveChartToPdfTask.execute()`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

详情页导出、记录预生成。

## 注意事项

使用副本重排 index，不原地修改数据库解压出的波形点；PDF 报告按记录时间从左到右绘制。`co2Unit` 控制波形指标、纵轴单位和页脚 EtCO2 参考值单位；`deviceSerial` 为空时模板设备编号显示 `未填写`。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
