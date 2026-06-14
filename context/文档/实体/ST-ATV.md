<!-- context-seed:start -->
# ActionsTabView

## 定位

- ID: `ST-ATV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:66`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ActionsTabView` 是 iOS 端的底部导航 TabView，包含三个 Tab：
  - **PageTypes.SearchDeviceList (0)**: 附近设备搜索页 (`SearchDeviceListView`)。
  - **PageTypes.Result (1)**: 主页波形和数据显示 (`ResultView`)。
  - **PageTypes.Config (2)**: 设置一级页 (`ConfigView`)。
- 每个 Tab 根据 `selectedTabIndex` 切换图标（`_active` 后缀表示选中态）。
- 通过 `@Binding var selectedTabIndex: Int` 双向绑定当前 Tab 索引。
- 在 `onAppear` 中通过 `UITabBarItem.appearance()` 设置 Tab 字体大小。

## 调用链

- 在 `BasePageView` 的 `body` 底部被包含。
- Tab 切换时，`selectedTabIndex` 的变化会向上传递到 `BasePageView`。
- `SearchDeviceListView` 在连接成功后会将 `selectedTabIndex` 切换到 `PageTypes.Result`。

## 使用建议

- 通过 `PageTypes` 枚举值而非直接 Int 设置 Tab 索引。
- Tab 的顺序和图标资源是固定的，添加新 Tab 需同步更新枚举和图标资源。
<!-- context-seed:end -->
