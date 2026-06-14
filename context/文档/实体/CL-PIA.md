<!-- context-seed:start -->
# PDFImageAnnotation

## 定位

- ID: `CL-PIA`
- 类型: `class` (PDFAnnotation)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:27`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `PDFImageAnnotation` 是一个自定义 PDF 注解子类，用于在 PDF 页面上绘制 UIImage。
- 继承 `PDFAnnotation`，重写 `draw(with:in:)` 方法。
- 持有 `image: UIImage?` 属性，在渲染时通过 `context.draw(cgImage, in: bounds)` 绘制。
- 便利构造器接收 image、bounds 和 properties 参数。

## 调用链

- 在 `View.asImage()` 扩展中被用于生成 PDF 可用的图像数据。
- 为 `HistoryDataManage._savePDFToLocal()` 的 PDF 渲染提供图像注解支持。

## 使用建议

- 目前无 active 调用方（代码中存在但未使用），保留以备后续 PDF 图像注解需求。
<!-- context-seed:end -->
