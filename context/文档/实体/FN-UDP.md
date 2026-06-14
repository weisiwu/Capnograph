<!-- context-seed:start -->
# updateDisplayParams

## 定位

- ID: `FN-UDP`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1388`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `updateDisplayParams(_ CO2Unit: CO2UnitType, _ CO2Scale: CO2ScaleEnum)` 是更新显示参数函数。
- 向蓝牙设备发送 CO2 单位和刻度设置指令。
- 在 `DisplayConfigView` 中保存设置时调用。
- 与 Android 端 `BlueToothKit.updateDisplayParams()` 对应。
<!-- context-seed:end -->
