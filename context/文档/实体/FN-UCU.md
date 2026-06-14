<!-- context-seed:start -->
# updateCO2Unit

## 定位

- ID: `FN-UCU`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:696`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `updateCO2Unit(cb: @escaping () -> Void)` 是更新 CO2 单位的函数。
- 向蓝牙设备发送 CO2 单位变更指令。
- 更新成功后在回调 `cb` 中完成后续操作。
- 在 `DisplayConfigView` 中更新设置时调用。
- 与 Android 端的单位更新指令对应。
<!-- context-seed:end -->
