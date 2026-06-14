<!-- context-seed:start -->
# connect

## 定位

- ID: `FN-CONN`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1375`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `connect(to: CBPeripheral, cb: @escaping (Bool) -> Void)` 是连接蓝牙设备函数。
- 调用 `CBCentralManager.connect()` 连接指定的外设。
- 回调 `cb` 在连接成功或失败后返回结果。
- 在 `SearchDeviceListView` 中用户确认连接设备时调用。
- 与 Android 端 `BlueToothKit.connectDevice()` 对应。
<!-- context-seed:end -->
