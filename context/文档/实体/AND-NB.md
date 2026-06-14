<!-- context-seed:start -->
# NavBar

## 定位

- ID: `AND-NB`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/NavBar.kt:44`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `NavBar` 是 Android 端的顶部导航栏组件。
- 显示当前页面标题（来自 `PageScene` 枚举）。
- 根据场景显示"返回"箭头、右侧按钮（用于记录历史等）。
- 使用 `NavBarComponentState` 管理导航状态。

## 调用链

- 在 `BaseLayout` 顶部渲染。
- 与 iOS 端的 `NavigationStack` + `.navigationTitle()` 对应。
<!-- context-seed:end -->
