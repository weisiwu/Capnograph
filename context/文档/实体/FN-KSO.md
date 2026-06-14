<!-- context-seed:start -->
# keepScreenOn

## 定位

- ID: `FN-KSO`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:848`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `keepScreenOn(isKeepScreenOn: Bool)` 是屏幕常亮控制函数。
- 根据传入的布尔值启用或禁用屏幕常亮。
- 在 `BasePageView` 中通过 `onChange(of: bluetoothManager.isKeepScreenOn)` 调用。
- 使用 `UIApplication.shared.isIdleTimerDisabled` 实现。
<!-- context-seed:end -->
