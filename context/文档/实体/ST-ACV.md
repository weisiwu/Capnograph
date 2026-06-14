<!-- context-seed:start -->
# AlertConfigView

## 定位

- ID: `ST-ACV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AlertConfigView.swift:127`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AlertConfigView` 是 iOS 端的报警参数设置页面。
- 通过 `BaseConfigContainerView(configType: ConfigItemTypes.Alert)` 包裹。
- **两个可配置参数**：
  - ETCO2 范围：使用 `RangeSlider` 选择上下限。
  - RR 范围：使用 `RangeSlider` 选择上下限。
- **更新流程**：
  1. 点击"更新"按钮，先检查蓝牙连接状态。
  2. 调用 `checkAlertRangeValid()` 验证范围有效性。
  3. 调用 `updateAlertRange()` 通过蓝牙同步到设备。
  4. 显示成功 Toast。
- 进入页面时从蓝牙管理器读取当前报警范围值。

## 调用链

- 从 `ConfigView` 导航栏"报警设置"按钮推送进入。
- 与 Android 端的 `AlertSettingActivity` 对应。
<!-- context-seed:end -->
