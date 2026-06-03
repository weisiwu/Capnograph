# trendStep

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L381）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：trendStep
- ID / 别名：trend data step, 趋势步长
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：当前代码值为 100

## 补充职责

趋势数据采样步长。

## 关键 ID / 别名

- 定位别名：trend data step, 趋势步长
- 关键字段 / 方法：`100`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`100`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

实时 chunk 写入、`stopRecord`、`insertCO2DataForRecord`。

## 注意事项

chunk 大小同为 100 时每块通常只产生一个趋势点。

## 最小验证方式

`rg "trendStep"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
