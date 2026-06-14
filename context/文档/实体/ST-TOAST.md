<!-- context-seed:start -->
# Toast

## 定位

- ID: `ST-TOAST`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:8`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `Toast` 是一个 SwiftUI View，用于在屏幕底部居中显示短暂的提示信息。
- 接收 `message: String` 和 `type: ToastType` 参数。
- 当 `type == .SUCCESS` 时显示 `toast_success` 图标，否则显示 `toast_fail` 图标。
- 容器为黑色半透明圆角矩形 (`Color.black.opacity(0.8)`, `cornerRadius(4)`)，文字和图标为白色。
- 始终全屏背景透明，通过 `.transition(.opacity)` 实现淡入淡出动画。
- 生命周期由 `AppConfigManage.toastMessage` 控制：非空字符串时展示，置空时隐藏。

## 调用链

- 在 `ContentView.swift` 的 `BasePageView` 中被条件渲染：`appConfigManage.toastMessage != ""`。
- 其他 View（如 `SearchDeviceListView`、`ConfigView`、`ResultView`）通过设置 `appConfigManage.toastMessage` 触发 Toast 显示。
- 通常配合 0.5~1 秒的 `DispatchQueue.main.asyncAfter` 自动清空消息。

## 使用建议

- 想要显示 Toast 时设置 `appConfigManage.toastMessage = "xxx"` 和 `appConfigManage.toastType = .SUCCESS/.FAIL`。
- 不要在短时间内连续触发多个 Toast，后一个会覆盖前一个。
<!-- context-seed:end -->
