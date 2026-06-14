<!-- context-seed:start -->
# SplashScreen

## 定位

- ID: `AND-SS`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/SplashActivity.kt:35`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SplashScreen(onNavigateToMain: () -> Unit)` 是应用启动时显示的闪屏 UI。
- 使用 `animateFloatAsState` 实现 500ms 的 alpha 渐变效果（0→1）。
- 使用 `LaunchedEffect` 设置 1.5 秒延迟后调用导航回调。
- 布局：白色背景全屏居中显示 WLD Logo（宽度为屏幕宽度减 100dp）。
- 不依赖任何 Service 或 Manager，纯 UI 展示。

## 调用链

- 在 `SplashActivity.setContent` 中创建。
<!-- context-seed:end -->
