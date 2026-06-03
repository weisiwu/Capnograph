# Realtime CO2 ingestion flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L109）。
领域：数据流。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Realtime CO2 ingestion flow
- ID / 别名：dataFlow, 波形数据流, 实时监测
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：BLE 通知解析为 CO2/RR/ETCO2/FICO2 后带采样时间戳更新 StateFlow 和实时波形

## 补充职责

将 BLE 通知帧解析为实时 CO2/RR/ETCO2/FiCO2，并在记录缓存的 CO2WavePointData 上写入 `sampleTimeMillis = System.currentTimeMillis()`，驱动图表与记录缓存。

## 关键 ID / 别名

- 定位别名：dataFlow, 波形数据流, 实时监测
- 关键字段 / 方法：`receivedArray`、`getCMDDataArray`、`getSpecificValue`、`handleCO2Waveform`、`dataFlow`、`totalCO2WavedDataFlow`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`receivedArray`、`getCMDDataArray`、`getSpecificValue`、`handleCO2Waveform`、`dataFlow`、`totalCO2WavedDataFlow`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`

## 主要调用点

`BluetoothGattCallback.onCharacteristicChanged`、`EtCo2LineChart.LaunchedEffect(blueToothKit.dataFlow)`。

## 注意事项

`dataFlow` 只保留 `maxXPoints = 500`；长期记录由 `AppStateModel.totalCO2WavedData` 缓存后写 Room。实时图表的 `DataPoint` 仍使用滚动 index，历史 PDF 波形使用 `CO2WavePointData.sampleTimeMillis` 还原真实时间轴。

## 最小验证方式

`rg "updateTotalCO2WavedData|dataFlow"`.

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
