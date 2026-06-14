<!-- context-seed:start -->
# LoadingView

## 定位

- ID: `ST-LV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:40`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LoadingView` 是一个全屏覆盖的加载遮罩层，接收 `loadingText: String?` 参数。
- 当 `loadingText != nil` 时显示：半透明黑色背景 (`Color.black.opacity(0.4)`) + 白色旋转进度指示器 (`ProgressView`) + 顶部加载文字。
- 当 `loadingText == nil` 时返回 `EmptyView()`，不渲染任何内容。
- 在 `BasePageView` 中通过 `.overlay()` 修饰符叠加：`appConfigManage.loadingMessage != ""` 时显示。
- 生命周期由 `AppConfigManage.loadingMessage` 控制。

## 调用链

- `SearchDeviceListView` 在搜索设备前设置 `appConfigManage.loadingMessage`。
- `ConfigView` 在校零、关机等操作时设置。
- `DisplayConfigView`、`ModuleConfigView` 等在更新设置时使用。
- 操作完成后通过 `appConfigManage.loadingMessage = ""` 解除遮罩。

## 使用建议

- 任何需要阻塞用户交互的操作都应显示 LoadingView。
- 务必在异步操作完成后清空 `loadingMessage`，否则遮罩不会消失。
<!-- context-seed:end -->
