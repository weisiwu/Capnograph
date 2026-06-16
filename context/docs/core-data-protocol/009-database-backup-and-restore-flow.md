# Database backup and restore flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L122）。
领域：存储。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Database backup and restore flow
- ID / 别名：db backup, restore, 数据库备份恢复
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：首次安装/手动恢复/普通启动时处理数据库 DB、WAL、SHM 文件备份恢复

## 补充职责

App 启动、首次安装或手动恢复时备份/恢复 DB、WAL、SHM 文件。

## 关键 ID / 别名

- 定位别名：db backup, restore, 数据库备份恢复
- 关键字段 / 方法：`DatabaseBackupHelper.startWork`、`backupDatabase`、`restoreDatabase`、`IS_FIRST_LAUNCH`、`BACKUP_FAILURE_COUNT`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`DatabaseBackupHelper.startWork`、`backupDatabase`、`restoreDatabase`、`IS_FIRST_LAUNCH`、`BACKUP_FAILURE_COUNT`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`

## 主要调用点

`CapnoEasyApplication.onCreate`；`MainActivity.onActivityResult` 获取文件管理权限后强制恢复。

## 注意事项

备份路径为 Downloads/`CapnoEasyApplicationDatabaseBackup`；连续失败 3 次停止尝试。启动备份/恢复、复制备份和恢复数据库异常会通过 ErrorReporter 上报为非致命异常。

## 最小验证方式

检查 `DatabaseBackupHelperKit.kt`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
