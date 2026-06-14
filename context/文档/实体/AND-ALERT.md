<!-- context-seed:start -->
# AlertModal

## 定位

- ID: `AND-ALERT`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/AlertModal.kt:44`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AlertModal(data: AlertData)` 是 Android 端的双按钮 Alert 弹框组件。
- 半透明遮罩 + 居中弹框，显示消息文本和"确定"/"取消"两个按钮。
- "确定"按钮触发 `data.onOk`，"取消"按钮触发 `data.onCancel` 并关闭弹框。

## 调用链

- 在 `BaseActivity.ShowAlert()` 中渲染。
- 与 iOS 端的 `.alert()` 系统弹框对应。
<!-- context-seed:end -->
