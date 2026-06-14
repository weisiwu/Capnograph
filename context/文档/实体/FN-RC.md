<!-- context-seed:start -->
# registerCharacteristic

## 定位

- ID: `FN-RC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:438`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `registerCharacteristic(characteristics: [CBCharacteristic], for service: CBService)` 是特征值注册函数。
- 在 `peripheral(_:didDiscoverCharacteristicsFor:error:)` 委托回调中调用。
- 遍历特征值列表，根据 UUID 匹配发送特征值、接收特征值、反劫持特征值。
- 为需要通知的特征值设置通知（`setNotifyValue(true)`）。

## 调用链

- 服务发现后自动调用。
- 注册完成后调用 `sendContinuous()` 开始数据采集。
- 与 Android 端 `BlueToothKit.gattCallback.onServicesDiscovered()` 中的特征值匹配逻辑对应。
<!-- context-seed:end -->
