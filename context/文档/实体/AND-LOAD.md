<!-- context-seed:start -->
# Loading

## 定位

- ID: `AND-LOAD`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/Loading.kt:43`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `Loading(data: LoadingData, onClick, onTimeout)` 是 Android 端的全屏加载遮罩组件。
- 显示半透明遮罩 + 居中加载指示器 + 提示文字。
- `cancelable=true` 时点击遮罩可关闭，同时触发 `onClick` 回调。
- 超时自动关闭并调用 `onTimeout`。

## 调用链

- 在 `BaseActivity.ShowLoading()` 中渲染。
- 与 iOS 端的 `LoadingView` 对应。
<!-- context-seed:end -->
