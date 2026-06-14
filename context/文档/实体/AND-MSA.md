<!-- context-seed:start -->
# ModuleSettingActivity

## 定位

- ID: `AND-MSA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/ModuleSettingActivity.kt:24`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ModuleSettingActivity` 是 Android 端的模块参数设置页面（`PageScene.MODULE_CONFIG_PAGE`）。
- 提供窒息时间（`asphyxiationTime`）和氧气补偿（`o2Compensation`）两个参数的配置。
- 使用滑块组件选择参数值。
- 更新后通过蓝牙调用 `updateNoBreathAndGasCompensation()` 同步到设备。

## 调用链

- 从 `SettingActivity` 菜单点击进入。
- 与 iOS 端的 `ModuleConfigView` 对应。
<!-- context-seed:end -->
