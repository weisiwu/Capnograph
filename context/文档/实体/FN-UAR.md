<!-- context-seed:start -->
# updateAlertRange

## 定位

- ID: `FN-UAR`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:779`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `updateAlertRange(type: String, value: CGFloat, upper: CGFloat, lower: CGFloat)` 是报警范围更新函数。
- 更新 ETCO2 或 RR 的报警上下限值到蓝牙设备。
- 先调用 `checkAlertRangeValid()` 验证范围有效性。
- 在 `AlertConfigView` 中保存设置时调用。
- 与 Android 端的报警范围更新指令对应。
<!-- context-seed:end -->
