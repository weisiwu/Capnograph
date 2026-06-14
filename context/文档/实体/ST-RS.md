<!-- context-seed:start -->
# RangeSlider

## 定位

- ID: `ST-RS`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AlertConfigView.swift:5`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `RangeSlider` 是 iOS 端报警范围设置的双滑块选择器组件。
- 包含上下两个滑块，分别选择最小值和最大值。
- 使用 `GeometryReader` 计算滑块位置。
- `calculateOffsetX` 函数计算滑块偏移量。

## 调用链

- 在 `AlertConfigView` 中用于 ETCO2 和 RR 的报警范围设定。
- 与 Android 端的 `RangeSelector` 组件对应。
<!-- context-seed:end -->
