<!-- context-seed:start -->
# CapnoGraphApp

## 定位

- ID: `ST-CGA`
- 类型: `struct` (App)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/CapnoGraphApp.swift:5`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CapnoGraphApp` 是 iOS 端 SwiftUI 应用的入口结构体，遵循 `App` 协议。
- `@State private var showSplash = true` 控制启动流程：先展示 `SplashView`（闪屏），约 2 秒后切换为 `ContentView`。
- `WindowGroup` 中根据 `showSplash` 状态决定渲染哪个根视图。
- 应用生命周期：`SplashView`（渐入动画品牌展示）→ `ContentView`（含 `BasePageView` + `ActionsTabView`）。
- 与 Android 端的 `CapnoEasyApplication` 对应。

## 使用建议

- 如需修改启动流程，在此调整 Splash 展示时长和切换逻辑。
- 如需全局注入，在此通过 `@StateObject` 创建 `BluetoothManager` 和 `AppConfigManage` 实例，通过 `.environmentObject()` 注入到子视图。
<!-- context-seed:end -->
