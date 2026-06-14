<!-- context-seed:start -->
# CO2WavePointData

## 定位

- ID: `ST-CWP`
- 类型: `struct`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:70`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CO2WavePointData` 定义了 CO2 波形数据的单点数据结构。
- **属性**：
  - `co2: Float`: CO2 浓度值。
  - `RR: Int`: 呼吸率值，无值时置 0。
  - `ETCO2: Float`: 呼气末 CO2 值，无值时置 0。
  - `FiCO2: Int`: 吸入 CO2 值。
  - `index: Int`: 点位的序号索引。

## 调用链

- `HistoryData.CO2WavePoints` 的类型为此结构体数组。
- `LineChartViewForImage` 使用此数据绘制波形折线图。
- `HistoryDataManage.syncBluetoothManagerData()` 从 `BluetoothManager` 同步波形数据到此结构。
- 注意：iOS 端的此结构体与 Android 端的同名 data class 定义略有不同（Android 版多了 `sampleTimeMillis` 字段）。
<!-- context-seed:end -->
