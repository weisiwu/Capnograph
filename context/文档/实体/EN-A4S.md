<!-- context-seed:start -->
# A4Size

## 定位

- ID: `EN-A4S`
- 类型: `enum` (Double)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:9`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `A4Size` 定义了 A4 纸张的尺寸常量（基于 PDFKit 的默认单位 1 pt = 1/72 inch）。
- **case height = 595.2**: A4 纸高度（mm 换算为 pts）。
- **case width = 841.8**: A4 纸宽度（mm 换算为 pts）。
- 在 PDF 导出流程中用于页面边界计算。

## 调用链

- `LineChartViewForImage` 使用 `A4Size` 计算图表渲染区域。
- `HistoryDataManage._savePDFToLocal()` 使用 `A4Size` 设置 PDF 页面尺寸。
- `View.asImage()` 扩展使用 `A4Size` 定义导出图像的分辨率尺寸。

## 使用建议

- 如需更换纸张类型（如 Letter），添加新的 case 并更新相关计算。
<!-- context-seed:end -->
