<!-- context-seed:start -->
# BaseConfigContainerView

## 定位

- ID: `ST-BCCV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:16`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BaseConfigContainerView<Content: View>` 是所有二级设置页面的通用容器包装视图。
- 接收 `configType: ConfigItemTypes` 和 `@ViewBuilder content` 闭包。
- 包含统一的安全区域处理、蓝牙连接状态按钮、校零按钮和关机按钮（位于页面底部）。
- `handleShutdown` / `handleSetZero` / `handleSetZeroFail` 是此容器内关联的按钮操作处理函数。
- `checkBluetoothStatus()` 在操作前检查蓝牙连接状态。

## 使用建议

- 所有设置页面（系统、显示、报警、模块）都通过此容器包裹，确保统一的布局和操作按钮。
<!-- context-seed:end -->
