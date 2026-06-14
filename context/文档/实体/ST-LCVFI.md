<!-- context-seed:start -->
# LineChartViewForImage

## 定位

- ID: `ST-LCVFI`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:147`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LineChartViewForImage` 是一个用于 PDF 导出的波形折线图视图，与 `LineChartView`（实时显示）功能类似但专为 PDF 图片优化。
- **参数**：
  - `data: HistoryData`: 历史数据源。
  - `xStart` / `xEnd`: 当前页的横坐标起止范围。
  - `blm: BluetoothManager`: 用于获取 CO2 刻度等设备参数。
  - `fSize: CGFloat = 14`: 坐标轴标签字号。
- 使用 Swift Charts 的 `Chart` + `LineMark` 绘制波形，插值方式为 `.catmullRom`。
- Y 轴刻度通过 `generateYAxis(scale:)` 生成。
- 每页固定展示 500 数据点，分页由 `_savePDFToLocal()` 控制。

## 调用链

- 在 `HistoryDataManage._savePDFToLocal()` 中被 `ImageRenderer` 包裹生成 PDF 图像。
- 数据来源于 `totalCO2WavedData.chunked(into: 500)` 的分块结果。
- 在 PDF 渲染循环中为每页数据创建一个实例。

## 使用建议

- 修改每页点数时更新 `pagePointsNumber` 常量和 X 轴步长 `xPointStep`。
- PDF 的叠加水印文字在此视图上层渲染（水印使用旋转文字层叠）。
<!-- context-seed:end -->
