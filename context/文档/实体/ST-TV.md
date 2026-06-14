<!-- context-seed:start -->
# TableView

## 定位

- ID: `ST-TV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:80`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `TableView` 是 iOS 端主页的实时数据信息表格，位于 `LineChartView` 下方。
- **四行信息**：
  - 设备名称 (`MainDeviceName`)：`bluetoothManager.connectedPeripheral?.name`。
  - 设备 ID (`MainDeviceID`)：`bluetoothManager.connectedPeripheral?.identifier.uuidString`。
  - 呼吸率 RR (`MainPR`)：`bluetoothManager.RespiratoryRate`，格式 `xxbpm/min`。
  - 呼气末 CO2 (`MainETCO2`)：`bluetoothManager.ETCO2`，格式 `xx.xxunit`。
- 使用 `HStack` 布局，左侧粗体标签 + 右侧细体数值。
- 数值为 0 时显示 "--" + 单位占位。

## 调用链

- 在 `ResultView` 中位于波形图下方展示。
- 数据由 `BluetoothManager` 持续实时更新。
- 所有标签文字通过 `appConfigManage.getTextByKey()` 本地化。

## 使用建议

- 添加新的数据行时，按相同的 HStack 模式拼接。
- 注意 0 值的占位显示逻辑（"--" 替换）。
<!-- context-seed:end -->
