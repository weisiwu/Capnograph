<!-- context-seed:start -->
# TypeSwitch

## 定位

- ID: `AND-TS`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/TypeSwitch.kt:90`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `TypeSwitch` 是 Android 端的连接方式选择器组件。
- 提供 BLE、WIFI、USB 三种连接方式的切换。
- 使用 `DeviceTypeList` 枚举定义可选项，`DeviceType` 数据类表示每种连接类型。

## 调用链

- 在搜索页面中用于选择搜索方式。
- 与 iOS 端连接方式选择对应。
<!-- context-seed:end -->
