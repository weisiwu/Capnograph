<!-- context-seed:start -->
# BluetoothManager

## 定位

- ID: `CL-BM`
- 类型: `class` (NSObject, CBCentralManagerDelegate, CBPeripheralDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:250`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BluetoothManager` 是 iOS 端的蓝牙通信核心类，继承 `NSObject`。
- 遵循 `CBCentralManagerDelegate` 和 `CBPeripheralDelegate` 协议。
- **核心功能**：
  - 蓝牙扫描、连接、断开管理。
  - 数据发送（`sendContinuous`、`sendStopContinuous`）与接收（`receivePeripheralData`）。
  - 设备信息获取（`getDeviceInfo`）和初始化（`initDevice`）。
  - 参数更新（CO2 单位、刻度、报警范围、窒息时间、氧气补偿等）。
  - 校零（`correctZero`）和关机（`shutdown`）指令。
  - 波形数据处理（`handleCO2Waveform`、`handleCO2Status`）。
  - 音频报警（`playAlertAudio`、`stopAudio`）。
- 使用 `NotificationCenter` 发布数据更新通知，`AppConfigManage.listenToBluetoothManager()` 订阅。

## 调用链

- 在 `CapnoGraphApp` 中以 `@StateObject` 创建，通过 `.environmentObject()` 注入。
- 通过 `CentralManager` 的 `BluetoothDevice.extra` 扩展属性（类似 Android 的 BLEDeviceExtra）管理设备状态。
- 与 Android 端的 `BlueToothKit` 对应。
<!-- context-seed:end -->
