# CO2Data

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L375）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：CO2Data
- ID / 别名：`co2_data` table, CO2数据块
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：分块压缩的波形数据

## 补充职责

波形数据块表。

## 关键 ID / 别名

- 定位别名：`co2_data` table, CO2数据块
- 关键字段 / 方法：table `co2_data`; `recordId` 外键; `chunkIndex`; `trendData`; BLOB `data`。

## 关键字段 / 方法

- 主要字段、方法或协议值：table `co2_data`; `recordId` 外键; `chunkIndex`; `trendData`; BLOB `data`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

实时记录写入、历史详情读取。

## 注意事项

`recordId + chunkIndex` 唯一，Record 删除级联删除数据块。

## 最小验证方式

检查实体注解

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
