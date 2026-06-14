<!-- context-seed:start -->
# peripheral (didUpdateNotificationStateFor)

## 定位

- ID: `FN-PERI-05`
- 类型: `function` (CBPeripheralDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1238`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `peripheral(_:didUpdateNotificationStateFor:error:)` 是实现 `CBPeripheralDelegate` 协议的通知状态更新回调。
- 在特征值通知订阅状态变化时被系统调用。
- 确认通知已成功开启或关闭。
<!-- context-seed:end -->
