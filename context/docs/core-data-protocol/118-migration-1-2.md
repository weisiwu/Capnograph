# MIGRATION_1_2

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L379）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：MIGRATION_1_2
- ID / 别名：Room migration 1 to 2, 数据库迁移
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：v1 到 v2 的 Room 迁移

## 补充职责

v1 到 v2 迁移。

## 关键 ID / 别名

- 定位别名：Room migration 1 to 2, 数据库迁移
- 关键字段 / 方法：重建 `records`，新建 `co2_data`，旧 `data` 分块压缩。

## 关键字段 / 方法

- 主要字段、方法或协议值：重建 `records`，新建 `co2_data`，旧 `data` 分块压缩。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt`

## 主要调用点

Room 自动迁移。

## 注意事项

`trendData` 迁移时为空。

## 最小验证方式

检查迁移脚本

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
