<!-- context-seed:start -->
# ActionBar

## 定位

- ID: `AND-AB`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/ActionBar.kt:43`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ActionBar` 是 Android 端的底部导航栏（TabBar）组件。
- **三个 Tab**：搜索（搜索图标）→ 主页（首页图标）→ 设置（设置图标）。
- 使用 `TabItem` 数据类定义每个 Tab 的文字、图标和索引。
- Tab 切换通过 `viewModel.onTabClick(index)` 管理。

## 调用链

- 在 `BaseLayout` 底部渲染。
- 与 iOS 端的 `ActionsTabView` 对应。
<!-- context-seed:end -->
