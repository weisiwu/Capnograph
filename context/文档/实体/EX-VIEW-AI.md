<!-- context-seed:start -->
# View.asImage

## 定位

- ID: `EX-VIEW-AI`
- 类型: `extension` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:273`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `View.asImage()` 是 SwiftUI View 扩展，将当前视图渲染为 `UIImage`。
- 使用 `UIHostingController` 包裹视图以获取渲染上下文。
- 输出尺寸为 A4 纸大小（595.2 × 841.8 pts），内部内容区域留边距 30pt。
- 渲染流程：
  1. 创建 `UIHostingController` 并设置 frame。
  2. 使用 `UIGraphicsImageRenderer` 以高分辨率渲染。
  3. 翻转坐标系（PDF 坐标系与 UIKit 坐标系相反）。
  4. 调用 `drawHierarchy(in:afterScreenUpdates:)` 绘制。

## 调用链

- 用于 `HistoryDataManage._savePDFToLocal()` 的 PDF 页面生成。
- 注意：当前实际 PDF 渲染已改用 `ImageRenderer`（iOS 16+），此扩展可能已不再被直接调用。

## 使用建议

- `ImageRenderer`（iOS 16+）是更推荐的 SwiftUI 视图导出方式。
- 此扩展保留以兼容更低版本 iOS 的场景。
<!-- context-seed:end -->
