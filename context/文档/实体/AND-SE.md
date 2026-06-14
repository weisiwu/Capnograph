<!-- context-seed:start -->
# SearchActivity

## 定位

- ID: `AND-SE`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/SearchActivity.kt:24`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SearchActivity` 是 Android 端的设备搜索页面（`PageScene.DEVICES_LIST_PAGE`）。
- **核心流程**：
  1. 用户点击"搜索"按钮 → `blueToothKit.searchDevices()` 启动搜索。
  2. 扫描到设备通过 `DeviceList` 组件展示。
  3. 用户点击设备 → `blueToothKit.connectDevice()` 连接。
  4. 连接成功后跳转到 `MainActivity` 并保存配对设备信息。
- 空状态时通过 `DeviceList` 组件展示空提示。
- 搜索时显示 Loading 遮罩（InfinityDuration 表示手动取消为止）。

## 调用链

- 从 `SettingActivity` 或底部 Tab 导航进入。
- 与 iOS 端的 `SearchDeviceListView` 对应。
<!-- context-seed:end -->
