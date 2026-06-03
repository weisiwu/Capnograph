# DatabaseBackupHelper.startWork

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L342）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`DatabaseBackupHelper.startWork`
- ID / 别名：backup worker entry, 备份恢复入口
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：判断首次安装/恢复/备份失败次数后选择恢复或备份

## 补充职责

判断首次安装、手动恢复、备份失败次数并选择恢复或备份。

## 关键 ID / 别名

- 定位别名：backup worker entry, 备份恢复入口
- 关键字段 / 方法：`IS_FIRST_LAUNCH`、`DATA_LATEST_VERSION`、`BACKUP_FAILURE_COUNT`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`IS_FIRST_LAUNCH`、`DATA_LATEST_VERSION`、`BACKUP_FAILURE_COUNT`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`

## 主要调用点

Application 启动、MainActivity 权限回调。

## 注意事项

使用 `runBlocking`。

## 最小验证方式

检查 startWork

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
