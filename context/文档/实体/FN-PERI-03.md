<!-- context-seed:start -->
# peripheral (didUpdateValueFor)

## 定位

- ID: `FN-PERI-03`
- 类型: `function` (CBPeripheralDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1226`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `peripheral(_:didUpdateValueFor:error:)` 是实现 `CBPeripheralDelegate` 协议的特征值更新回调。
- 在蓝牙设备发送数据到手机时被系统调用。
- 调用 `receivePeripheralData()` 处理接收到的数据。
- 与 Android 端 `BlueToothKit.gattCallback.onCharacteristicChanged()` 对应。
<!-- context-seed:end -->
