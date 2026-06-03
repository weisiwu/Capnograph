# DatabaseBackupHelper.restoreDatabase

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L347）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`DatabaseBackupHelper.restoreDatabase`
- ID / 别名：restore DB, 恢复数据库
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：删除当前 DB/WAL/SHM，从 Download 备份目录复制恢复并重建 Room 实例

## 补充职责

删除当前 DB/WAL/SHM，从 Downloads 备份目录复制恢复并重建 Room。

## 关键 ID / 别名

- 定位别名：restore DB, 恢复数据库
- 关键字段 / 方法：`AppDatabase.clearInstance`、`roomDatabase.close()`、`copyTo`、`AppDatabase.getDatabase`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`AppDatabase.clearInstance`、`roomDatabase.close()`、`copyTo`、`AppDatabase.getDatabase`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

首次安装/手动恢复。

## 注意事项

假定 WAL/SHM 备份文件存在，copy 时可能抛异常并被外层捕获。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
