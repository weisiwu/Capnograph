<!-- context-seed:start -->
# ContentView

## 定位

- ID: `ST-CONTENT`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:161`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ContentView` 是 iOS 端 SwiftUI 应用的根视图入口，直接渲染 `BasePageView {}`（空内容容器）。
- 实际页面内容由 `BasePageView` 内部的 `ActionsTabView` 根据 Tab 切换动态决定。
- iOS 应用的启动周期：`SplashView` → `ContentView`，由 `CapnoGraphApp.swift` 中的 `@State showSplash` 控制。

## 调用链

- `CapnoGraphApp.swift` 在 `WindowGroup` 中根据 `showSplash` 状态决定展示 `SplashView` 还是 `ContentView`。
- 启动后再无其他处直接引用 `ContentView`。

## 使用建议

- 不要修改 ContentView 的结构，它是应用入口的稳定锚点。
- 如需调整页面布局，应修改 `BasePageView` 或 `ActionsTabView`。
<!-- context-seed:end -->
