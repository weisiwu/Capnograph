<!-- context-seed:start -->
# BaseLayout

## 定位

- ID: `AND-BL`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/BaseLayout.kt:34`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BaseLayout` 是 Android 端最外层的页面容器 composable。
- 组合结构：顶部 `NavBar` → 内容区 → 底部 `ActionBar`（底部导航栏）。
- 子类 Activity 的 `Content()` 嵌入内容区。

## 调用链

- 在 `BaseActivity.setContent` 中渲染，包裹 `Content()`。
- 每个 Activity 的页面标题通过 `PageScene` 枚举动态生成。
<!-- context-seed:end -->
