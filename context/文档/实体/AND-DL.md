<!-- context-seed:start -->
# DeviceList

## 定位

- ID: `AND-DL`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/DeviceList.kt:54`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `DeviceList` 是 Android 端的设备列表组件，用于搜索页面。
- 显示发现的蓝牙设备列表，每个设备显示名称和 UUID。
- 支持空状态提示、搜索按钮、设备点击连接。
- 每个设备项右侧有"连接"按钮。

## 调用链

- 在 `SearchActivity.Content()` 中使用。
- 与 iOS 端的 `SearchDeviceListView` 内容对应。
<!-- context-seed:end -->
