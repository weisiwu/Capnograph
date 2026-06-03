# Patient

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L373）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：Patient
- ID / 别名：`patients` table, 患者
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：Room 实体

## 补充职责

患者表。

## 关键 ID / 别名

- 定位别名：`patients` table, 患者
- 关键字段 / 方法：table `patients`; fields `name`、`gender`、`age`、auto `id`。

## 关键字段 / 方法

- 主要字段、方法或协议值：table `patients`; fields `name`、`gender`、`age`、auto `id`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

记录开始时插入。

## 注意事项

`gender` 使用自定义 enum 经 Room 转换。

## 最小验证方式

检查实体定义

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
