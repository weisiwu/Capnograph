<!-- context-seed:start -->
# resizeImage

## 定位

- ID: `FN-RESIMG`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ModuleConfigView.swift:3`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `resizeImage(_:targetSize:)` 是一个全局工具函数，用于缩放 `UIImage` 到目标尺寸。
- 使用 `UIGraphicsBeginImageContextWithOptions` 和 `UIGraphicsGetImageFromCurrentImageContext` 实现。
- 在 `SingleSliderInner` 中被调用，将 `slider_right` 图片缩放到按钮大小 (`btnSize: CGFloat = 30`)。
- 返回 `UIImage?`，为 `UISlider` 设置自定义滑块按钮。

## 使用建议

- 当前仅用于 UISlider 的自定义滑块按钮渲染。
- 如需在其他地方缩放 UIImage，直接复用此函数。
<!-- context-seed:end -->
