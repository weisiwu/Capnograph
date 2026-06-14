<!-- context-seed:start -->
# DisplaySettingActivity

## 定位

- ID: `AND-DSA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/DisplaySettingActivity.kt:24`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `DisplaySettingActivity` 是 Android 端的显示参数设置页面（`PageScene.DISPLAY_CONFIG_PAGE`）。
- 提供 CO2 单位、CO2 刻度范围、波形速度（WF Speed）三个参数的配置。
- 使用 `WheelPicker` 滚轮选择器选择参数值。
- 更新后通过蓝牙同步到设备。

## 调用链

- 从 `SettingActivity` 菜单点击进入。
- 与 iOS 端的 `DisplayConfigView` 对应。
<!-- context-seed:end -->
