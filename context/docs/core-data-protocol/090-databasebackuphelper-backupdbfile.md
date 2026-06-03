# DatabaseBackupHelper.backupDBFile

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L344）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`DatabaseBackupHelper.backupDBFile`
- ID / 别名：backup db file, 备份 DB
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：使用 MediaStore 新建或更新主数据库备份文件

## 补充职责

使用 MediaStore 新建或覆盖主 DB 备份文件。

## 关键 ID / 别名

- 定位别名：backup db file, 备份 DB
- 关键字段 / 方法：Display name = `DATABASE_NS`、MIME `application/octet-stream`。

## 关键字段 / 方法

- 主要字段、方法或协议值：Display name = `DATABASE_NS`、MIME `application/octet-stream`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`

## 主要调用点

`backupDatabase`。

## 注意事项

路径为 Downloads/备份目录。

## 最小验证方式

检查 ContentValues

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
