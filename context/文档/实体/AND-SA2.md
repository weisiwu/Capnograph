<!-- context-seed:start -->
# SettingActivity

## 定位

- ID: `AND-SA2`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/SettingActivity.kt:21`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SettingActivity` 是 Android 端的设置一级页（`PageScene.SETTING_PAGE`）。
- `Content()` 渲染 `SettingList` 组件，显示所有设置菜单项。
- 菜单项点击后跳转到对应的二级设置页面。

## 调用链

- 与 iOS 端的 `ConfigView` 对应。
<!-- context-seed:end -->
