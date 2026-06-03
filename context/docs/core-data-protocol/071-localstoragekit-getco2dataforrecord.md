# LocalStorageKit.getCO2DataForRecord

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L325）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.getCO2DataForRecord`
- ID / 别名：read waveform flow, 流式读取波形
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：从 CO2Data Flow 解压并发出波形点列表

## 补充职责

从 CO2Data Flow 解压并逐块 emit。

## 关键 ID / 别名

- 定位别名：read waveform flow, 流式读取波形
- 关键字段 / 方法：`getCO2DataByRecordIdFlow`、`emit`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`getCO2DataByRecordIdFlow`、`emit`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

可用于流式读取。

## 注意事项

每个 chunk 单独 emit，不合并全量。

## 最小验证方式

检查 flow

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
