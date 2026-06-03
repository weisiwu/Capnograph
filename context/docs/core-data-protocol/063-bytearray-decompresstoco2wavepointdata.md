# ByteArray.decompressToCO2WavePointData

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L317）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`ByteArray.decompressToCO2WavePointData`
- ID / 别名：GZIP decompress, 波形解压
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：GZIP 解压并用 Gson 还原 CO2WavePointData 列表

## 补充职责

GZIP 解压并反序列化波形列表。

## 关键 ID / 别名

- 定位别名：GZIP decompress, 波形解压
- 关键字段 / 方法：`GZIPInputStream`、`TypeToken<List<CO2WavePointData>>`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`GZIPInputStream`、`TypeToken<List<CO2WavePointData>>`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

历史详情、读取 Flow。

## 注意事项

输入必须是该压缩格式。

## 最小验证方式

`rg "decompressToCO2WavePointData"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
