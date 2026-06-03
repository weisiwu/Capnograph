# Thermal print flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L116）。
领域：打印。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Thermal print flow
- ID / 别名：hotmelt print, ticket, 热敏打印
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：详情页筛选波形数据，调用 GPrinter SDK 输出小票和旋转波形图

## 补充职责

历史详情页读取全量 CO2Data，过滤波形并调用 GPrinter SDK 打印小票和旋转波形图。

## 关键 ID / 别名

- 定位别名：hotmelt print, ticket, 热敏打印
- 关键字段 / 方法：`onPrintTicketClick`、`filterData`、`gpPrinterManager.print`、`HotmeltPinter.startProcessingData`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`onPrintTicketClick`、`filterData`、`gpPrinterManager.print`、`HotmeltPinter.startProcessingData`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

详情页右上角；`viewModel.isPDF == false`。

## 注意事项

打印前要求 `gpPrinterManager.getConnectState()` 为 true；打印机通过经典蓝牙 GP 名称自动连接。

## 最小验证方式

`rg "gpPrinterManager.print|getConnectState"`.

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
