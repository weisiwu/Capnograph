<!-- context-seed:start -->
# ConfigView

## 定位

- ID: `ST-CV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:114`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ConfigView` 是 iOS 端的设置一级页，位于 `ActionsTabView` 的第三个 Tab（`PageTypes.Config`）。
- 通过 `BaseConfigContainerView` 包裹，底部包含蓝牙连接状态、校零和关机按钮。
- **设置菜单项**（通过 `ConfigItem` 渲染）：
  - 显示设置 → `DisplayConfigView`
  - 报警设置 → `AlertConfigView`
  - 模块参数 → `ModuleConfigView`
  - 打印设置（iOS 暂未实现）
  - 系统设置 → `SystemConfigView`
  - 设备列表 → `SearchDeviceListView`
- 通过 `NavigationStack` 管理二级页面的导航堆栈。
- 校零/关机操作通过 `bluetoothManager.correctZero()` / `bluetoothManager.shutdown()` 执行。

## 调用链

- 底部 Tab 导航进入。
- 与 Android 端的 `SettingActivity` + `SettingList` 对应。
<!-- context-seed:end -->
