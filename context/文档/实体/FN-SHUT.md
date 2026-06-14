<!-- context-seed:start -->
# shutdown

## 定位

- ID: `FN-SHUT`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:658`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `shutdown()` 是传感器关机函数。
- 向蓝牙设备发送关机指令（`SensorCommand.Control`）。
- 设备接收到指令后自动关闭电源。
- 在 `ConfigView` 中的"关机"按钮触发时调用。
- 与 Android 端的关机指令对应。
<!-- context-seed:end -->
