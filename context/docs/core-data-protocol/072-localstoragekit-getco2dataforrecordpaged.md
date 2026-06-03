# LocalStorageKit.getCO2DataForRecordPaged

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L326）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.getCO2DataForRecordPaged`
- ID / 别名：paged waveform read, 分页读取波形
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：分页读取 CO2Data chunk 并解压

## 补充职责

分页读取 CO2Data 并解压合并。

## 关键 ID / 别名

- 定位别名：paged waveform read, 分页读取波形
- 关键字段 / 方法：`pageSize = 10`、`LIMIT/OFFSET`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`pageSize = 10`、`LIMIT/OFFSET`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

历史数据读取备用。

## 注意事项

offset 按 chunk 页数递增。

## 最小验证方式

检查 while

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
