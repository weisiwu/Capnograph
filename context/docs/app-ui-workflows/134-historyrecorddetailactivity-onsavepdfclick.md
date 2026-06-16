# HistoryRecordDetailActivity.onSavePDFClick

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L276）。
领域：页面函数。

## 实体定位

- 实体：`HistoryRecordDetailActivity.onSavePDFClick`
- ID / 别名：save PDF action, 保存 PDF
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：读取记录所有 CO2 数据、打印偏好和当前设备序列号并异步生成 PDF

## 补充职责

读取记录所有 CO2 数据、打印偏好和当前 `BlueToothKit.sSerialNumber`，并异步生成 PDF；协程异常通过 ErrorReporter 上报。

## 关键 ID / 别名

save PDF action, 保存 PDF

## 关键字段 / 方法

- 主要实体或方法：`HistoryRecordDetailActivity.onSavePDFClick`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

由对应 Activity 生命周期、导航栏按钮、保存按钮或 Compose 内容回调触发。

## 注意事项

读取记录所有 CO2 数据、打印偏好和当前设备序列号并异步生成 PDF；设备序列号传给 `saveChartToPdfInBackground(deviceSerial=...)`，用于 PDF 基础信息区的设备编号。错误上报 metadata 不直接携带设备序列号或患者字段。

## 最小验证方式

./gradlew :app:assembleDebug；人工触发对应按钮/生命周期路径。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
