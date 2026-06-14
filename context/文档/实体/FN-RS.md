<!-- context-seed:start -->
# registerService

## 定位

- ID: `FN-RS`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:387`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `registerService(service: CBService)` 是 BLE 服务注册函数。
- 在 `peripheral(_:didDiscoverServices:)` 委托回调中调用。
- 遍历发现的 BLE 服务，根据 UUID 匹配发送数据服务、接收数据服务、反劫持服务和模块参数服务。
- 匹配成功后调用 `registerCharacteristic()` 注册服务对应的特征值。

## 调用链

- 蓝牙连接成功并发现服务后自动调用。
- 与 Android 端 `BlueToothKit.gattCallback.onServicesDiscovered()` 中的服务匹配逻辑对应。
<!-- context-seed:end -->
