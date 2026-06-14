<!-- context-seed:start -->
# LoadingData

## 定位

- ID: `AND-LD`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/Loading.kt:32`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LoadingData` 是 Loading 遮罩的数据模型。
- **属性**：`text: String`（加载提示文字）、`duration: Long`（最长等待时间）、`cancelable: Boolean`（是否可取消）、`onTimeout: (() -> Unit)?`（超时回调）。

## 调用链

- 由 `viewModel.updateLoadingData(LoadingData(...))` 触发。
- `Loading` composable 根据此数据渲染，超时后自动执行 `onTimeout`。
<!-- context-seed:end -->
