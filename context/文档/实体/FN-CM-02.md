<!-- context-seed:start -->
# centralManager (didConnect)

## 定位

- ID: `FN-CM-02`
- 类型: `function` (CBCentralManagerDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1191`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `centralManager(_:didConnect:)` 是实现 `CBCentralManagerDelegate` 协议的连接成功回调函数。
- 在外设连接成功后被系统调用。
- 开始对外设进行服务发现（`discoverServices`）。
- 与 Android 端 `BlueToothKit.gattCallback.onConnectionStateChange()` 的 `STATE_CONNECTED` 分支对应。
<!-- context-seed:end -->
