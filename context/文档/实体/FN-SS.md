<!-- context-seed:start -->
# startScanning

## 定位

- ID: `FN-SS`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1341`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `startScanning(cb: @escaping () -> Void)` 是启动蓝牙扫描的函数。
- 调用 `CBCentralManager.scanForPeripherals(withServices:)` 开始扫描 BLE 设备。
- 扫描结果通过 `centralManager(_:didDiscover:)` 回调获取。
- 在 `SearchDeviceListView` 中点击"搜索"按钮时调用。
- 与 Android 端 `BlueToothKit.searchDevices()` 对应。
<!-- context-seed:end -->
