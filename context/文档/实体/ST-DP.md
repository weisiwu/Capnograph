<!-- context-seed:start -->
# DataPoint

## 定位

- ID: `ST-DP`
- 类型: `struct` (Identifiable)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:13`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `DataPoint` 是 CO2 波形图使用的单个数据点结构，遵循 `Identifiable` 协议。
- **属性**：
  - `id = UUID()`: 唯一标识（自动生成）。
  - `value: Float`: CO2 浓度值。
- 在 `LineChartView` 中通过 `bluetoothManager.receivedCO2WavedData` 数组中的 `DataPoint` 实例绘制波形。

## 调用链

- `BluetoothManager.receivedCO2WavedData` 的类型为 `[DataPoint]`。
- `LineChartView` 使用 `ForEach(Array(bluetoothManager.receivedCO2WavedData.enumerated()), id: \.offset)` 遍历并绘制。
- 数据由蓝牙模块实时接收并更新。

## 使用建议

- 注意与 `CO2WavePointData`（HistoryDataManage 中的历史数据点）的区别：`DataPoint` 是实时显示用的轻量结构。
<!-- context-seed:end -->
