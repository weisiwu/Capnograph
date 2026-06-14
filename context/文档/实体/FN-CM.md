<!-- context-seed:start -->
# centralManager (didDiscover)

## 定位

- ID: `FN-CM`
- 类型: `function` (CBCentralManagerDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1182`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `centralManager(_:didDiscover:advertisementData:rssi:)` 是实现 `CBCentralManagerDelegate` 协议的发现设备回调函数。
- 在蓝牙扫描发现新外设时被系统调用。
- 将发现的设备加入 `discoveredPeripherals` 列表。
- 与 Android 端 `BlueToothKit.leScanCallback.onScanResult()` 对应。
<!-- context-seed:end -->
