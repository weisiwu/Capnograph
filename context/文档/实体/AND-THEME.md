<!-- context-seed:start -->
# CapnoEasyTheme

## 定位

- ID: `AND-THEME`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Theme.kt:37`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CapnoEasyTheme(content: @Composable () -> Unit)` 是 Android 端的主题 composable 顶层组件。
- 使用 Material3 的 `MaterialTheme` 配置应用级主题色和字体。
- 使用 `Color.kt` 中定义的颜色常量和 `Type.kt` 中的排版定义。
- 支持深色/浅色主题切换。

## 调用链

- 在 `MainActivity.setContent()` 中包裹主页 UI。
- 所有 Material3 组件通过此主题获取样式。
<!-- context-seed:end -->
