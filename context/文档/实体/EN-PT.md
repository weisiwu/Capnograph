<!-- context-seed:start -->
# PageTypes

## 定位

- ID: `EN-PT`
- 类型: `enum` (Int)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:104`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `PageTypes` 定义了 iOS 端所有一级导航页面的枚举映射。
- **case NilPage = -1**: 非页面标记，用于配置页中特殊按钮操作（如蓝牙连接、校零）。
- **case SearchDeviceList = 0**: 设备搜索列表页。
- **case Result = 1**: 主页，显示 CO2 波形图表和数据表格。
- **case Config = 2**: 设置一级页。
- `rawValue` 为 Int 类型，与 `selectedTabIndex` 绑定。

## 调用链

- `ActionsTabView` 使用 `PageTypes` 的 rawValue 作为 TabView 的 `selection` 和 `.tag()`。
- `BasePageView` 通过 `PageTypes.Result.rawValue` 初始化选中 Tab。
- `SearchDeviceListView.swift` 在蓝牙连接成功后通过 `selectedTabIndex = PageTypes.Result.rawValue` 跳转到主页。
- `ConfigView.swift` 通过 `selectedTabIndex = PageTypes.Config.rawValue` 跳转到设置页。
<!-- context-seed:end -->
