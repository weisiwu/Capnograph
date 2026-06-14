<!-- context-seed:start -->
# SingleSliderInner

## 定位

- ID: `ST-SSI`
- 类型: `struct` (UIViewRepresentable)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ModuleConfigView.swift:14`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SingleSliderInner` 是一个 UIKit `UISlider` 的 SwiftUI 桥接封装。
- **参数**：
  - `value: Binding<Double>`：绑定的滑块值。
  - `minimumValue: Float` / `maximumValue: Float`：滑块范围。
  - `btnSize: CGFloat = 30`：滑块按钮尺寸。
- 通过 `makeUIView` / `updateUIView` 桥接 UISlider 和 SwiftUI 生命周期。
- 自定义滑块按钮：使用 `slider_right` 图片（通过 `resizeImage` 缩放）。
- 值变化通过 `Coordinator` 的 `valueChanged` 回调更新绑定。

## 调用链

- 被 `SingleSlider` 包裹使用，不直接对外暴露。
- 用于 `ModuleConfigView` 的窒息时间和氧气补偿滑块。

## 使用建议

- 如果滑块样式需要修改，更换 `slider_right` 图片和 `btnSize`。
- 滑块颜色通过 `.accentColor` 在父级 `SingleSlider` 中设置。
<!-- context-seed:end -->
