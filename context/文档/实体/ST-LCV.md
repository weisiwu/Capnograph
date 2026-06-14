<!-- context-seed:start -->
# LineChartView

## 定位

- ID: `ST-LCV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:18`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LineChartView` 是 iOS 端主页的实时 CO2 波形折线图。
- 使用 Swift Charts 的 `Chart` + `LineMark` 绘制，插值方式为 `.cardinal`。
- **X 轴**：范围 0~`maxXPoints`(400)，每 `xPointStep`(50) 一个刻度，标签显示秒数。
- **Y 轴**：范围 0~`CO2Scale.rawValue`（根据当前 CO2 单位动态变化），刻度由 `generateYScale` 生成。
- 数据来源：`bluetoothManager.receivedCO2WavedData`（实时数据流）。
- 图表高度固定 300pt。

## 调用链

- 在 `ResultView` 中作为主页顶部的核心组件。
- `BluetoothManager` 持续接收蓝牙数据帧并更新 `receivedCO2WavedData`。
- 与 Android 端的 `EtCo2LineChart` 对应，共享相同的波形展示逻辑。

## 使用建议

- 图表 Y 轴范围跟随 CO2 单位变化（mmHg/百分比/KPa 各有不同刻度值）。
- X 轴步长 50 对应每个刻度约 2.5 秒（50 点 × 50ms 采样间隔）。
<!-- context-seed:end -->
