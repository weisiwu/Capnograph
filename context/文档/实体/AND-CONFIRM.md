<!-- context-seed:start -->
# ConfirmModal

## 定位

- ID: `AND-CONFIRM`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/ConfirmModal.kt:44`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ConfirmModal(data: ConfirmData)` 是 Android 端的单按钮确认弹框组件。
- 半透明遮罩 + 居中弹框，显示消息文本和一个确认按钮。
- 点击确认按钮触发 `data.callback` 并关闭弹框。

## 调用链

- 在 `BaseActivity.ShowConfirm()` 中渲染。
- 与 iOS 端的 `.alert()` 系统弹框（单按钮场景）对应。
<!-- context-seed:end -->
