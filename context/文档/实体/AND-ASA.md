<!-- context-seed:start -->
# AlertSettingActivity

## 定位

- ID: `AND-ASA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/AlertSettingActivity.kt:23`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AlertSettingActivity` 是 Android 端的报警参数设置页面（`PageScene.ALERT_CONFIG_PAGE`）。
- 提供 ETCO2 范围（`alertETCO2Range`）和 RR 范围（`alertRRRange`）的配置。
- 使用 `RangeSelector` 组件选择范围值。
- 更新后通过蓝牙发送到设备。

## 调用链

- 从 `SettingActivity` 菜单点击进入。
- 与 iOS 端的 `AlertConfigView` 对应。
<!-- context-seed:end -->
