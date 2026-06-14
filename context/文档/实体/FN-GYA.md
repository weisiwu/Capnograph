<!-- context-seed:start -->
# generateYAxis

## 定位

- ID: `FN-GYA`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:57`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `generateYAxis(scale: CO2ScaleEnum) -> [Double]` 是一个全局函数，根据 CO2 刻度枚举返回对应的 Y 轴刻度数组。
- **各刻度对应的刻度值**：
  - `.mmHg_Small`: [0,10,20,30,40,50] — 最大 50 mmHg
  - `.mmHg_Middle`: [0,10,20,30,40,50,60] — 最大 60 mmHg
  - `.mmHg_Large`: [0,10,20,30,40,50,60,70,75] — 最大 75 mmHg
  - `.KPa_Small`: [0,1,2,3,4,5,6,6.7] — 最大 6.7 kPa
  - `.KPa_Middle`: [0,1,2,3,4,5,6,7,8] — 最大 8 kPa
  - `.KPa_Large`: [0,1,2,3,4,5,6,7,8,9,10] — 最大 10 kPa
  - `.percentage_Small`: [0,1,2,3,4,5,6,6.6] — 最大 6.6%
  - `.percentage_Middle`: [0,1,2,3,4,5,6,7,7.9] — 最大 7.9%
  - `.percentage_Large`: [0,1,2,3,4,5,6,7,8,9,9.9] — 最大 9.9%

## 调用链

- `LineChartView` 的 Y 轴 `AxisMarks(values:)` 使用此函数生成刻度。
- `LineChartViewForImage`（PDF 导出）也使用此函数。

## 使用建议

- 添加新的刻度范围时，在 `CO2ScaleEnum` 添加新 case 并在此函数中配置对应刻度值。
<!-- context-seed:end -->
