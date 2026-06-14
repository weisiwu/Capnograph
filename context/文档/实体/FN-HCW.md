<!-- context-seed:start -->
# handleCO2Waveform

## 定位

- ID: `FN-HCW`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:941`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleCO2Waveform(data: [UInt8])` 是 CO2 波形数据处理函数。
- 解析蓝牙设备返回的 0xE1 类型波形数据帧。
- 提取 CO2 浓度值、呼吸率 RR 和 ETCO2 值。
- 将解析后的数据点追加到 `receivedCO2WavedData` 数组。
- 通过 `NotificationCenter` 通知 UI 更新波形图。

## 调用链

- 由 `receivePeripheralData()` 根据协议类型分发调用。
- 与 Android 端 `BlueToothKit` 中对应解析逻辑对应。
<!-- context-seed:end -->
