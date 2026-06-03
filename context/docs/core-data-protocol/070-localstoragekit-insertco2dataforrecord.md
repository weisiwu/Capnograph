# LocalStorageKit.insertCO2DataForRecord

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L324）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.insertCO2DataForRecord`
- ID / 别名：insert waveform chunks, 写入波形块
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：收集数据 Flow 并插入压缩 CO2Data；当前实时写入主要在 EtCo2LineChart 内完成

## 补充职责

从 Flow 收集数据块并插入 CO2Data。

## 关键 ID / 别名

- 定位别名：insert waveform chunks, 写入波形块
- 关键字段 / 方法：本地 `chunkSize = 6000`、`trendStep`、`compress`。

## 关键字段 / 方法

- 主要字段、方法或协议值：本地 `chunkSize = 6000`、`trendStep`、`compress`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

当前非主实时写入路径。

## 注意事项

`chunkSize` 未参与 chunk 切分。

## 最小验证方式

检查函数体

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
