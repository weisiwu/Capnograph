<!-- context-seed:start -->
# peripheral (didWriteValueFor)

## 定位

- ID: `FN-PERI-04`
- 类型: `function` (CBPeripheralDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1231`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `peripheral(_:didWriteValueFor:error:)` 是实现 `CBPeripheralDelegate` 协议的特征值写入回调。
- 在蓝牙数据发送到设备完成后被系统调用。
- 确认指令发送成功或处理发送失败。
<!-- context-seed:end -->
