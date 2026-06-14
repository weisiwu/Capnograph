<!-- context-seed:start -->
# peripheral (didDiscoverCharacteristicsFor)

## 定位

- ID: `FN-PERI-02`
- 类型: `function` (CBPeripheralDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1221`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `peripheral(_:didDiscoverCharacteristicsFor:error:)` 是实现 `CBPeripheralDelegate` 协议的特征值发现回调。
- 在服务的特征值发现完成后被系统调用。
- 调用 `registerCharacteristic()` 注册发现的特征值。
<!-- context-seed:end -->
