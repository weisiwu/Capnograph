<!-- context-seed:start -->
# View.shareableView

## 定位

- ID: `EX-VIEW-SH`
- 类型: `extension` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:144`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `View.shareableView(url: URL?)` 是一个 SwiftUI View 扩展，根据分享 URL 决定是否包裹 `ShareLink`。
- **逻辑**：如果 `url != nil`，用 `ShareLink(item: _url)` 包裹当前视图，使其点击后唤起系统分享面板；否则直接返回原视图。

## 调用链

- 在 `BottomSheetView` 中的 PDF 图标按钮上使用：`.shareableView(url: historyDataManage.pdfURL)`。
- PDF 生成完成前 `pdfURL` 为 nil，图标仅为占位展示（含旋转 loading 动画）。
- PDF 生成完成后 `pdfURL` 被设置，点击图标触发系统分享。

## 使用建议

- 如需为其他文件类型（如 CSV）添加分享入口，传入对应 URL 即可。
<!-- context-seed:end -->
