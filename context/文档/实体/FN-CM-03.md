<!-- context-seed:start -->
# centralManager (didFailToConnect)

## 定位

- ID: `FN-CM-03`
- 类型: `function` (CBCentralManagerDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1201`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `centralManager(_:didFailToConnect:error:)` 是实现 `CBCentralManagerDelegate` 协议的连接失败回调函数。
- 在外设连接失败后被系统调用。
- 重置连接状态并清理资源。
<!-- context-seed:end -->
