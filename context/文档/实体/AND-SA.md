<!-- context-seed:start -->
# SplashActivity

## 定位

- ID: `AND-SA`
- 类型: `class` (ComponentActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/SplashActivity.kt:16`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SplashActivity` 是 Android 端的启动闪屏 Activity。
- `onCreate` 中使用 `setContent { SplashScreen(onNavigateToMain = { ... }) }`。
- 启动后自动跳转到 `MainActivity` 并 `finish()` 关闭自身。

## 调用链

- AndroidManifest 中将 SplashActivity 设为启动 Activity。
- `SplashScreen` composable 在 1.5 秒后自动导航到 MainActivity。
- 与 iOS 端的 `SplashView` 对应。
<!-- context-seed:end -->
