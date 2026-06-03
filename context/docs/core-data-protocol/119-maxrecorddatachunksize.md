# maxRecordDataChunkSize

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L380）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：maxRecordDataChunkSize
- ID / 别名：chunk size, 数据块大小
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：当前代码值为 100

## 补充职责

当前实时记录 chunk 点数。

## 关键 ID / 别名

- 定位别名：chunk size, 数据块大小
- 关键字段 / 方法：`100`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`100`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`EtCo2LineChart`、`stopRecord`、迁移 chunked。

## 注意事项

注释里旧值 10000 保留；代码当前值为 100。

## 最小验证方式

`rg "maxRecordDataChunkSize ="`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
