# Recording lifecycle flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L111）。
领域：记录。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Recording lifecycle flow
- ID / 别名：start record, stop record, 记录开始停止
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：主页开始/停止记录，创建 Patient/Record，按 chunk 写入 CO2Data

## 补充职责

主页开始/停止记录，创建 Patient/Record，保存患者基本信息和剩余波形。

## 关键 ID / 别名

- 定位别名：start record, stop record, 记录开始停止
- 关键字段 / 方法：`MainActivity.onNavBarRightClick`、`LocalStorageKit.savePatient`、`saveRecord`、`stopRecord`、`currentRecordId`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`MainActivity.onNavBarRightClick`、`LocalStorageKit.savePatient`、`saveRecord`、`stopRecord`、`currentRecordId`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`

## 主要调用点

主页右侧导航按钮。

## 注意事项

`saveRecord` 创建记录；实时 CO2Data chunk 写入不在 `saveRecord`，而在 `EtCo2LineChart`。

## 最小验证方式

检查 `viewModel.isRecording` 分支

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
