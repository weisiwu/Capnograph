# LocalStorageKit.stopRecord

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L323）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.stopRecord`
- ID / 别名：finish record, 停止记录
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：停止时保存不足一个 chunk 的剩余波形并清空 `currentRecordId`

## 补充职责

保存不足一个 chunk 的剩余波形，清空当前记录 ID。

## 关键 ID / 别名

- 定位别名：finish record, 停止记录
- 关键字段 / 方法：`remainData`、`CO2Data`、`currentRecordId = null`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`remainData`、`CO2Data`、`currentRecordId = null`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

主页停止记录。

## 注意事项

chunkIndex 用当前记录已有 chunk 数量。

## 最小验证方式

检查 `remainData.isNotEmpty()`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
