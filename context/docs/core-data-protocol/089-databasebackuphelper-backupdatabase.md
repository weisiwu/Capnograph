# DatabaseBackupHelper.backupDatabase

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L343）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`DatabaseBackupHelper.backupDatabase`
- ID / 别名：backup DB files, 备份数据库文件
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：关闭数据库后备份 DB、WAL、SHM 到 Download 目录

## 补充职责

关闭数据库并备份 DB/WAL/SHM。

## 关键 ID / 别名

- 定位别名：backup DB files, 备份数据库文件
- 关键字段 / 方法：`roomDatabase.close()`、`backupDBFile`、`backupWALFile`、`backupSHMFile`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`roomDatabase.close()`、`backupDBFile`、`backupWALFile`、`backupSHMFile`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`

## 主要调用点

`startWork` 非首次安装分支。

## 注意事项

备份后没有在本函数内重新打开数据库。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
