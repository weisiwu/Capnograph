<!-- context-seed:start -->
# WheelPicker

## 定位

- ID: `AND-WP`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/WheelPicker.kt:36`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `WheelPicker` 是 Android 端的滚轮选择器组件。
- 接收 `wheelPickerConfig` 配置（标题、选项列表、默认值）。
- 用于显示设置页的 CO2 单位、刻度、WF Speed 等参数选择。

## 调用链

- 在 `DisplaySettingActivity` 中使用。
- 与 iOS 端的 SwiftUI `Picker(.wheel)` 对应。
<!-- context-seed:end -->
