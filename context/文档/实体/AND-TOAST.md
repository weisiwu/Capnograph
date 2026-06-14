<!-- context-seed:start -->
# Toast

## 定位

- ID: `AND-TOAST`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/Toast.kt:58`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `Toast(data: ToastData, onClick, onTimeout)` 是 Android 端的 Toast 全局组件。
- 显示在屏幕底部，自动消失（基于 duration）。
- `showMask` 控制是否显示遮罩层阻止用户交互。
- 支持点击关闭（`onClick`）和超时自动关闭（`onTimeout`）。

## 调用链

- 在 `BaseActivity.ShowToast()` 中渲染。
- 由 `viewModel.updateToastData(data)` 控制显示，`updateToastData(null)` 隐藏。
<!-- context-seed:end -->
