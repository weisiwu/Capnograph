# List<CO2WavePointData>.compress

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L316）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`List<CO2WavePointData>.compress`
- ID / 别名：GZIP compress, 波形压缩
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：Gson 序列化后 GZIP 压缩波形列表，包含新记录的采样时间戳

## 补充职责

Gson 序列化后 GZIP 压缩波形列表；新记录的 CO2WavePointData 包含 `sampleTimeMillis`，会一并写入 BLOB。

## 关键 ID / 别名

- 定位别名：GZIP compress, 波形压缩
- 关键字段 / 方法：`Gson().toJson`、`GZIPOutputStream`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`Gson().toJson`、`GZIPOutputStream`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`EtCo2LineChart`、`stopRecord`、迁移。

## 注意事项

返回 ByteArray 存入 Room BLOB。`sampleTimeMillis` 不新增 Room 列，属于压缩 JSON 内容。

## 最小验证方式

检查扩展函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
