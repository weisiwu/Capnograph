<!-- context-seed:start -->
# BasePageView

## 定位

- ID: `ST-BPV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:111`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BasePageView<Content: View>` 是 iOS 端最外层的页面容器，泛型 Content 接收任意 SwiftUI View。
- **两个构造方式**：
  - 无参构造：默认选中 `PageTypes.Result`（主页）。
  - `systemConfig:` 构造：传入 `PageTypes` 指定初始 Tab。
- 布局结构：顶部白色背景 → `content`（传入的页面内容）→ 底部 `ActionsTabView`（Tab 导航栏）。
- 覆盖层：`loadingMessage != ""` 时叠加 `LoadingView`；`toastMessage != ""` 时叠加 `Toast`。
- 通过 `onChange(of: bluetoothManager.isKeepScreenOn)` 控制屏幕常亮。

## 调用链

- `ContentView` 直接使用 `BasePageView {}`。
- `ConfigView` 中的二级页面（系统设置、显示设置等）通过 `NavigationStack` 推送，不经过 BasePageView。
- `AppConfigManage` 的 `loadingMessage`、`toastMessage`、`toastType` 控制覆盖层的显示状态。

## 使用建议

- `ContentView` 是唯一入口，不要在其他地方重复创建 BasePageView。
- 如需在非 Tab 页展示 Loading 或 Toast，直接设置对应的 AppConfigManage 属性即可。
<!-- context-seed:end -->
