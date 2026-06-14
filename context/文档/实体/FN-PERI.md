<!-- context-seed:start -->
# peripheral (didDiscoverServices)

## 定位

- ID: `FN-PERI`
- 类型: `function` (CBPeripheralDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1216`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `peripheral(_:didDiscoverServices:)` 是实现 `CBPeripheralDelegate` 协议的服务发现回调。
- 在外设服务发现完成后被系统调用。
- 调用 `registerService()` 注册发现的 BLE 服务。
- 与 Android 端 `BlueToothKit.gattCallback.onServicesDiscovered()` 对应。
<!-- context-seed:end -->
