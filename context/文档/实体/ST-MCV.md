<!-- context-seed:start -->
# ModuleConfigView

## 定位

- ID: `ST-MCV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ModuleConfigView.swift:191`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ModuleConfigView` 是 iOS 端的模块参数配置页面，包含窒息时间和氧气补偿设置。
- 通过 `BaseConfigContainerView(configType: ConfigItemTypes.Alert)` 包裹。
- **两个可配置参数**：
  - 窒息时间 (`asphyxiationTime`)：范围 `bluetoothManager.asphyxiationTimeMin` ~ `asphyxiationTimeMax`。
  - 氧气补偿 (`oxygenCompensation`)：范围 `oxygenCompensationMin` ~ `oxygenCompensationMax`，单位 `%`。
- **更新流程**：
  1. 点击"更新"按钮，先检查蓝牙连接状态。
  2. 写入 `UserDefaults`。
  3. 调用 `bluetoothManager.updateNoBreathAndGasCompensation()` 同步到设备。
  4. 0.5 秒后显示成功 Toast。
- 进入页面时从 `bluetoothManager` 读取当前值。

## 调用链

- 从 `ConfigView` 导航栏"模块参数"按钮推送进入。
- 数据源：`bluetoothManager.asphyxiationTime`、`bluetoothManager.oxygenCompensation`。
- 滑块组件使用 `SingleSlider`。

## 使用建议

- 蓝牙未连接时更新操作会被拦截并提示失败。
- 窒息时间整数输入，氧气补偿浮点数输入（1 位小数）。
<!-- context-seed:end -->
