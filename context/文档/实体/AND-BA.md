<!-- context-seed:start -->
# BaseActivity

## 定位

- ID: `AND-BA`
- 类型: `open class` (ComponentActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/BaseActivity.kt:52`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BaseActivity` 是 Android 端所有 Activity 的基类，继承 `ComponentActivity`，使用 `@AndroidEntryPoint`。
- **生命周期**：`onCreate()` 中初始化 ViewModel、BlueToothKit、LocalStorageKit、PrintProtocalKit。
- **Composable 渲染**（子类可覆盖）：
  - `ShowLoading()`: Loading 遮罩层。
  - `ShowAlert()`: 双按钮 Alert 弹框。
  - `ShowConfirm()`: 单按钮 Confirm 弹框。
  - `ShowToast()`: Toast 提示。
  - `ShowActionModal()`: 底部动作面板。
  - `Content()`: 子类页面内容（默认空实现）。
- **核心方法**：
  - `checkHasConnectDevice()`: 检查是否已连接 CapnoGraph 设备。
  - `checkBluetoothPermissions()`: 检查并请求蓝牙权限。
  - `onBackPressed()`: 退出的二次确认（仅单 Activity 且在主页时弹出确认弹框）。
  - `onTabClick()`: Tab 点击事件（正在记录时弹框确认）。
  - `updatePageScene()`: 导航栏标题更新。

## 调用链

- 所有 Android Activity 类继承 BaseActivity。
- 使用 `BaseLayout` 包裹页面内容，在 `setContent` 中组合 `NavBar` + `Content()` + 覆盖层。
- 与 iOS 端的 `BasePageView` + `NavigationStack` 组合对应。
<!-- context-seed:end -->
