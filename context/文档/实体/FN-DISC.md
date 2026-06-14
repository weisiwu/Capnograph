<!-- context-seed:start -->
# disconnect

## 定位

- ID: `FN-DISC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1367`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `disconnect()` 是断开蓝牙连接函数。
- 调用 `CBCentralManager.cancelPeripheralConnection()` 断开当前连接的外设。
- 在设备切换或用户主动断开时调用。
- 与 Android 端 `BlueToothKit.disconnectDevice()` 对应。
<!-- context-seed:end -->
