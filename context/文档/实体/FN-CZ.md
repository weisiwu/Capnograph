<!-- context-seed:start -->
# correctZero

## 定位

- ID: `FN-CZ`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:672`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `correctZero()` 是 CO2 传感器校零函数。
- 向蓝牙设备发送校零指令（`SensorCommand.Settings`），校准零点。
- 校零完成后设备自动回复状态确认。
- 在 `ConfigView` 中的"校零"按钮触发时调用。
- 与 Android 端的校零指令对应。
<!-- context-seed:end -->
