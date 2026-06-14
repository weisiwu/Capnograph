<!-- context-seed:start -->
# RangeSelector

## 定位

- ID: `AND-RS`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/RangeSelector.kt`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `RangeSelector` 是 Android 端的范围选择器组件。
- 提供最小值和最大值的双滑块选择。
- 用于报警设置页面的 ETCO2 和 RR 范围设定。

## 调用链

- 在 `AlertSettingActivity.Content()` 中使用。
- 与 iOS 端的 `RangeSlider`（AlertConfigView 中）对应。
<!-- context-seed:end -->
