<!-- context-seed:start -->
# listenToBluetoothManager

## 定位

- ID: `FN-LTBM`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:635`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `listenToBluetoothManager(bluetoothManager: BluetoothManager)` 是 iOS 端绑定蓝牙管理器的事件的函数。
- 使用 `NotificationCenter.default.publisher` 监听 `BluetoothManager` 发出的通知：
  - 更新 CO2 波形数据（`receivedCO2WavedData`）。
  - 更新呼吸率 RR 和 ETCO2 值。
  - 更新设备和模块参数。
  - 处理连接/断开事件。
- 所有蓝牙数据更新通过此函数桥接到 AppConfigManage 的 @Published 属性。

## 调用链

- 在 `CapnoGraphApp` 中由 `AppConfigManage` 的实例在初始化时调用。
- 与 Android 端 `BlueToothKit` 通过 `MutableStateFlow` 和 `AppStateModel` 桥接数据的方式对应。
<!-- context-seed:end -->
