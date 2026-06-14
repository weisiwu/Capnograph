<!-- context-seed:start -->
# ActionModal

## 定位

- ID: `AND-AM`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/ActionModal.kt:57`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ActionModal(viewModel, onCancelClick, onSavePDFClick, onPrintTicketClick)` 是 Android 端的底部弹出动作面板。
- 用于主页底部"更多"按钮，提供"导出 PDF"和"打印小票"两个操作。
- 显示在屏幕底部，半透明遮罩 + 白色圆角面板。

## 调用链

- 在 `BaseActivity.ShowActionModal()` 中渲染。
- 与 iOS 端的 `BottomSheetView` 对应。
<!-- context-seed:end -->
