# AppDatabase

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L372）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：AppDatabase
- ID / 别名：Room database, schema v2, Room 数据库
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/schemas/com.wldmedical.capnoeasy.kits.AppDatabase/1.json`
- 备注：数据库类；当前代码声明 version 2

## 补充职责

Room 数据库。

## 关键 ID / 别名

- 定位别名：Room database, schema v2, Room 数据库
- 关键字段 / 方法：entities: `Patient`、`Record`、`CO2Data`; version 2; converters: UUID/Patient/LocalDateTime。

## 关键字段 / 方法

- 主要字段、方法或协议值：entities: `Patient`、`Record`、`CO2Data`; version 2; converters: UUID/Patient/LocalDateTime。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

Application 初始化、恢复后重建。

## 注意事项

`exportSchema = false`，但项目仍有 v1 schema JSON。

## 最小验证方式

检查 `@Database`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
