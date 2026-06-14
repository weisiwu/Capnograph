<!-- context-seed:start -->
# SplashView

## 定位

- ID: `ST-SV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SplashView.swift:3`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SplashView` 是 iOS 端的启动闪屏页，使用渐入动画展示品牌信息。
- 布局垂直居中：WLD 图标 (`WLDIcon`, 277×153) → 中文公司名 "万联达信科" → 英文名 "WLD Instruments Co., Ltd"。
- `@State var fadeInOut` 控制所有元素的透明度，在 `onAppear` 中触发 `withAnimation(.easeIn(duration: 1))` 从 0 渐入到 1。
- 全屏白色背景，忽略安全区域。

## 调用链

- 在 `CapnoGraphApp.swift` 中展示：应用启动后先显示 `SplashView`，约 2 秒后通过 `@State showSplash` 切换为 `ContentView`。
- SplashView 不依赖任何 Service 或 Manager，纯 UI 展示。

## 使用建议

- 如需修改品牌信息，更新 `WLDIcon` 图片资源和文本内容。
- 注意动画时长与 `CapnoGraphApp.swift` 中的切换时延（约 2 秒）保持协调。
<!-- context-seed:end -->
