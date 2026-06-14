<!-- context-seed:start -->
# updateNoBreathAndGasCompensation

## 定位

- ID: `FN-UNBAGC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:817`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `updateNoBreathAndGasCompensation(newAsphyxiationTime: Int, newOxygenCompensation: Double)` 是窒息时间和氧气补偿更新函数。
- 同时更新窒息报警时间和氧气补偿值到蓝牙设备。
- 在 `ModuleConfigView` 中保存设置时调用。
- 与 Android 端 `BlueToothKit.updateNoBreathAndGasCompensation()` 对应。
<!-- context-seed:end -->
