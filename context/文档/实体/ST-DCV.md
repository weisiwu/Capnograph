<!-- context-seed:start -->
# DisplayConfigView

## 定位

- ID: `ST-DCV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/DisplayConfigView.swift:26`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `DisplayConfigView` 是 iOS 端的显示参数配置页面，包括 CO2 单位、刻度、波形速度设置。
- 通过 `BaseConfigContainerView(configType: ConfigItemTypes.System)` 包裹。
- **三个可配置参数**：
  - CO2 单位：`wheelPicker` 选择 mmHg / % / KPa。
  - CO2 刻度范围：联动 WheelPicker，单位改变时自动切换刻度选项集。
  - 波形速度 (WF Speed)：1 / 2 / 4 S 三档。
- **更新流程**：
  1. 点击"更新"按钮，写入 `UserDefaults`。
  2. 调用 `updateDisplayParams`（蓝牙发送参数给设备）。
  3. 调用 `updateCO2Unit` 回调，完成后重新发送连续数据。
- 接收多个闭包参数（`checkBluetoothStatus`、`updateCO2Unit`、`updateDisplayParams`、`sendContinuous`），由父级 `ConfigView` 注入。

## 调用链

- 从 `ConfigView` 导航栏"显示设置"按钮推送进入。
- 通过 `@StateObject paramsModel: CO2DisplayParamModel` 管理表单状态。
- 参数变化通过 `onReceive(paramsModel.$CO2Unit)` 监听并联动更新 `CO2Scales`。
- 使用 `isAppeared` 标志防止初始化时误触发联动。

## 使用建议

- 修改参数后需要蓝牙连接成功才能同步到设备。
- 若蓝牙未连接或状态异常，更新会提示失败。
<!-- context-seed:end -->
