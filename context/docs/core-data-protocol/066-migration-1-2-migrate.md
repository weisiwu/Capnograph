# MIGRATION_1_2.migrate

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L320）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`MIGRATION_1_2.migrate`
- ID / 别名：Room migration 1 to 2, 数据库迁移函数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：重建 records，创建 co2_data，并将旧 records 中的波形数据分块压缩迁移

## 补充职责

v1 records 拆分到 v2 records + co2_data。

## 关键 ID / 别名

- 定位别名：Room migration 1 to 2, 数据库迁移函数
- 关键字段 / 方法：`ALTER TABLE records RENAME TO records_old`、`CREATE co2_data`、`chunked(maxRecordDataChunkSize)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`ALTER TABLE records RENAME TO records_old`、`CREATE co2_data`、`chunked(maxRecordDataChunkSize)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt`

## 主要调用点

Room migration。

## 注意事项

迁移时 `trendData` 写空字符串。

## 最小验证方式

检查迁移 SQL

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
