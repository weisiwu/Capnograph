<!-- context-seed:start -->
# MainActivity

## 定位

- ID: `AND-MA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt:31`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `MainActivity` 是 Android 端的主页 Activity（`PageScene.HOME_PAGE`）。
- **onCreate 初始化**：
  1. 尝试自动连接已配对的 BLE 设备。
  2. 检查和管理文件读取权限（Android 11+ 需要）。
  3. 从本地存储加载打印设置和语言偏好。
- **onTabClick**：正在记录时阻止 Tab 切换并弹框确认。
- **Content()**：主页 UI，包含 `EtCo2LineChart`（波形图） + `EtCo2Table`（数据表格）。
- 主页顶部导航栏右侧按钮用于打开历史记录导出面板。
- 与 iOS 端的 `ResultView` 对应。

## 调用链

- `SplashActivity` 启动后通过 `Intent` 跳转到 `MainActivity`。
- `SearchActivity` 设备连接成功后也跳转到 `MainActivity`。
- 内容使用 `CapnoEasyTheme` 主题包裹。
<!-- context-seed:end -->
