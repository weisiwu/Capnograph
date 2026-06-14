<!-- context-seed:start -->
# SettingList

## 定位

- ID: `AND-SL`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/SettingList.kt:59`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SettingList` 是 Android 端设置页的菜单列表组件。
- 根据 `SettingType` 枚举渲染不同的设置项。
- **设置项包括**：系统设置、显示设置、报警设置、模块设置、打印设置、设备列表、历史记录、蓝牙校零、关机。

## 调用链

- 在 `SettingActivity.Content()` 中使用。
- 与 iOS 端的 `ConfigView` 配置列表对应。
<!-- context-seed:end -->
