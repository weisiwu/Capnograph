# DATABASE_NS

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L367）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：DATABASE_NS
- ID / 别名：`wld_medical_capnoeasy_database`
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：Room 数据库名

## 补充职责

Room 数据库名。

## 关键 ID / 别名

- 定位别名：`wld_medical_capnoeasy_database`
- 关键字段 / 方法：`wld_medical_capnoeasy_database`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`wld_medical_capnoeasy_database`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

`AppDatabase.getDatabase`、备份恢复。

## 注意事项

DB/WAL/SHM 文件名均基于该值。

## 最小验证方式

`rg "DATABASE_NS"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
