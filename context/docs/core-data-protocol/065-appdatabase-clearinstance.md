# AppDatabase.clearInstance

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L319）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`AppDatabase.clearInstance`
- ID / 别名：clear DB singleton, 清理数据库单例
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：恢复数据库前清空 Room 单例引用

## 补充职责

清空 Room 单例引用。

## 关键 ID / 别名

- 定位别名：clear DB singleton, 清理数据库单例
- 关键字段 / 方法：`INSTANCE = null`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`INSTANCE = null`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`

## 主要调用点

`DatabaseBackupHelper.restoreDatabase`。

## 注意事项

不主动 close，只清引用。

## 最小验证方式

`rg "clearInstance"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
