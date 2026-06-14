<!-- context-seed:start -->
# centralManager (didDisconnectPeripheral)

## 定位

- ID: `FN-CM-04`
- 类型: `function` (CBCentralManagerDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1206`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `centralManager(_:didDisconnectPeripheral:error:)` 是实现 `CBCentralManagerDelegate` 协议的设备断开回调函数。
- 在外设断开连接后被系统调用。
- 重置连接状态、清空设备引用、执行队列中的待处理任务。
- 与 Android 端 `BlueToothKit.gattCallback.onConnectionStateChange()` 的 `STATE_DISCONNECTED` 分支对应。
<!-- context-seed:end -->
