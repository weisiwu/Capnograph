<!-- context-seed:start -->
# SystemSettingActivity

## 定位

- ID: `AND-SSA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/SystemSettingActivity.kt:32`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SystemSettingActivity` 是 Android 端的系统设置页面（`PageScene.SYSTEM_CONFIG_PAGE`）。
- 显示语言切换（中/英文）和设备信息（固件版本、硬件版本、序列号、模块名称、生产日期）。
- `updateLanguage()` 方法通过 `AppCompatDelegate.setApplicationLocales()` 切换系统语言。

## 调用链

- 从 `SettingActivity` 菜单点击进入。
- 与 iOS 端的 `SystemConfigView` 对应。
<!-- context-seed:end -->
