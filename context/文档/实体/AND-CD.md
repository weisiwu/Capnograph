<!-- context-seed:start -->
# ConfirmData

## 定位

- ID: `AND-CD`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/ConfirmModal.kt:33`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ConfirmData` 是单按钮 Confirm 弹框的数据模型。
- **属性**：`text: String`（消息内容）、`btn_text: String`（按钮文字）、`callback: () -> Unit`（确认回调）。

## 调用链

- 由 `viewModel.updateConfirmData(ConfirmData(...))` 触发。
<!-- context-seed:end -->
