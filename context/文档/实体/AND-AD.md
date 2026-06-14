<!-- context-seed:start -->
# AlertData

## 定位

- ID: `AND-AD`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/AlertModal.kt:30`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AlertData` 是双按钮 Alert 弹框的数据模型。
- **属性**：`text: String`（消息内容）、`ok_btn_text: String` / `cancel_btn_text: String`（按钮文字）、`onOk: () -> Unit` / `onCancel: () -> Unit`（按钮回调）。

## 调用链

- 由 `viewModel.updateAlertData(AlertData(...))` 触发。
- `AlertModal` composable 根据此数据渲染。
<!-- context-seed:end -->
