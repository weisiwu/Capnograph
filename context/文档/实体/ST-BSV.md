<!-- context-seed:start -->
# BottomSheetView

## 定位

- ID: `ST-BSV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:157`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BottomSheetView` 是 iOS 端主页的底部弹出面板（ShareSheet），用于选择导出/分享操作。
- 通过 `.sheet(isPresented: $showModal)` 在 `ResultView` 中以 Modal 形式展示，高度 240pt。
- **核心交互**：
  - 面板顶部标题 ("ShareBtn")。
  - PDF 导出按钮：显示 `pdf_icon` 图标，数据准备中显示旋转 loading 动画（`pdf_icon_loading`）。
  - 导出完成后通过 `ShareLink`（由 `.shareableView` 封装）唤起系统分享面板。
  - 底部取消按钮 ("SearchConfirmNo")。
- `pdfURL` 通过 `historyDataManage.saveToLocal()` 异步生成。

## 调用链

- 在 `ResultView` 中通过 `showModal` 状态控制显示。
- `HistoryDataManage` 的 `saveToLocal()` 在 `.task` 中异步执行。
- 导出完成后 1 秒自动关闭面板（`DispatchQueue.main.asyncAfter`）。

## 使用建议

- PDF 生成期间显示 loading 动画阻止用户重复点击。
- 如需添加其他导出格式（CSV、图像），在此面板增加对应按钮。
<!-- context-seed:end -->
