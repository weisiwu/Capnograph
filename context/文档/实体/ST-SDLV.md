<!-- context-seed:start -->
# SearchDeviceListView

## 定位

- ID: `ST-SDLV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SearchDeviceListView.swift:4`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SearchDeviceListView` 是 iOS 端的设备搜索与连接页面，位于 `ActionsTabView` 的第一个 Tab。
- **核心流程**：
  1. 用户点击"搜索"按钮 → 调用 `bluetoothManager.startScanning()`。
  2. 搜索完成后，`discoveredPeripherals` 非空则显示设备列表。
  3. 用户点击设备上的"确认"按钮 → 弹出 Alert 确认连接。
  4. 确认后调用 `bluetoothManager.connect(to:)` → 连接成功切换 Tab 到 `PageTypes.Result`。
- 空状态时显示 `device_empty_list` 图片和提示文字。
- 通过 `@State var selectedPeripheral: CBPeripheral?` 持有选中设备。

## 调用链

- 顶部 `NavigationStack` 标题使用 `appConfigManage.getTextByKey(key: "TitleSearch")`。
- 所有文本通过 `@EnvironmentObject appConfigManage.getTextByKey()` 实现多语言切换。
- 连接前设置 `appConfigManage.loadingMessage`，连接完成后清空。

## 使用建议

- 搜索失败时展示 Toast 提示，1 秒后自动消失。
- 成功连接后自动跳转到主页（ResultView）。
<!-- context-seed:end -->
