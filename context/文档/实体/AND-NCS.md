<!-- context-seed:start -->
# NavBarComponentState

## 定位

- ID: `AND-NCS`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/NavBar.kt:36`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `NavBarComponentState` 是导航栏状态的数据类。
- **属性**：`currentPage: PageScene`（当前页面枚举值）。
- 导航栏根据此状态决定标题和导航按钮样式。
- 由 `AppState.currentPage` 持有，通过 `viewModel.updateCurrentPage()` 更新。
<!-- context-seed:end -->
