# Chunked waveform persistence flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L112）。
领域：存储。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Chunked waveform persistence flow
- ID / 别名：chunk storage, GZIP, trendData, 分块存储
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：`maxRecordDataChunkSize=100`，GZIP+Gson 压缩含采样时间戳的波形，`trendStep=100` 预存趋势数据

## 补充职责

将波形按 chunk 压缩写入 `co2_data`，并预留趋势数据字符串；新记录的每个 CO2WavePointData 会在 JSON 中携带 `sampleTimeMillis`。

## 关键 ID / 别名

- 定位别名：chunk storage, GZIP, trendData, 分块存储
- 关键字段 / 方法：`maxRecordDataChunkSize = 100`、`trendStep = 100`、`CO2Data(recordId, chunkIndex, trendData, data)`、`List<CO2WavePointData>.compress`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`maxRecordDataChunkSize = 100`、`trendStep = 100`、`CO2Data(recordId, chunkIndex, trendData, data)`、`List<CO2WavePointData>.compress`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`EtCo2LineChart` 实时 collect；`LocalStorageKit.stopRecord` 保存剩余数据。

## 注意事项

`trendData` 使用下划线拼接 ETCO2；chunk 唯一约束为 `recordId + chunkIndex`。`sampleTimeMillis` 存在于压缩 BLOB 内，不新增 Room 列，因此无需数据库迁移；旧 BLOB 缺失该字段时按 0 处理。

## 最小验证方式

检查 `CO2DataDao.insertCO2Data`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
