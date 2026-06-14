<!-- context-seed:start -->
# handleCO2Status

## 定位

- ID: `FN-HCS`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1009`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleCO2Status(data: [UInt8])` 是 CO2 设备状态处理函数。
- 解析蓝牙设备返回的 0x80 类型状态数据帧。
- 提取设备状态信息：窒息状态、低电量、适配器状态、呼吸状态、ETCO2/RR 有效标志等。
- 这些状态变量控制主页警告横幅的显示逻辑。

## 调用链

- 由 `receivePeripheralData()` 分发调用。
- 与 Android 端的状态解析逻辑对应。
<!-- context-seed:end -->
