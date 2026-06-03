# Record

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L374）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：Record
- ID / 别名：`records` table, 记录
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：Room 实体

## 补充职责

记录表。

## 关键 ID / 别名

- 定位别名：`records` table, 记录
- 关键字段 / 方法：table `records`; primary key UUID; `patient`、`startTime`、`endTime`、`dateIndex`、`patientIndex`、`pdfFilePath`。

## 关键字段 / 方法

- 主要字段、方法或协议值：table `records`; primary key UUID; `patient`、`startTime`、`endTime`、`dateIndex`、`patientIndex`、`pdfFilePath`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`saveRecord`、历史列表/详情。

## 注意事项

v2 移除了旧 `data` 字段，`previewPdfFilePath` 保留兼容。

## 最小验证方式

检查迁移

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
