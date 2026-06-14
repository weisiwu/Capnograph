<!-- context-seed:start -->
# ToastData

## 定位

- ID: `AND-TD`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/Toast.kt:48`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ToastData` 是 Toast 展示的数据模型。
- **属性**：`text: String`（消息内容）、`type: ToastType`（类型）、`showMask: Boolean`（是否显示遮罩）、`duration: Long`（时长）、`onTimeout: (() -> Unit)?`（超时回调）。

## 调用链

- 由 `viewModel.updateToastData(ToastData(...))` 触发显示。
- `Toast` composable 根据此数据渲染 UI。
- 与 iOS 端的 `appConfigManage.toastMessage` + `toastType` 对应。
<!-- context-seed:end -->
