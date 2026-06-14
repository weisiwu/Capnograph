<!-- context-seed:start -->
# ConfigItemTypes

## 定位

- ID: `EN-CIT`
- 类型: `enum`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:4`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ConfigItemTypes` 定义了设置页菜单项的类型枚举。
- **case System**: 系统设置，打开 `SystemConfigView`。
- **case Alert**: 报警参数设置。
- **case Display**: 显示参数设置。
- **case Module**: 模块参数设置。
- **case Print**: 打印设置。
- 用于 `ConfigView` 中通过 switch 分发到对应设置页面。

## 调用链

- `ConfigView` 的 `NavigationLink` 根据此枚举类型推送对应的二级设置页面。
- 与 Android 端的 `SettingType` 枚举对应。
<!-- context-seed:end -->
