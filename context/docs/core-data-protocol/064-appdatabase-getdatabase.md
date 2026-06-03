# AppDatabase.getDatabase

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L318）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`AppDatabase.getDatabase`
- ID / 别名：Room singleton, 获取数据库
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：双重检查创建 Room 数据库并添加 `MIGRATION_1_2`

## 补充职责

双重检查创建 Room 单例。

## 关键 ID / 别名

- 定位别名：Room singleton, 获取数据库
- 关键字段 / 方法：`DATABASE_NS`、`Room.databaseBuilder`、`addMigrations(MIGRATION_1_2)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`DATABASE_NS`、`Room.databaseBuilder`、`addMigrations(MIGRATION_1_2)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

Application 初始化、恢复后重建。

## 注意事项

如果实例存在且 `isOpen` 直接返回。

## 最小验证方式

检查 companion object

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
