<!-- context-seed:start -->
# xPointStep

## 定位

- ID: `VA-XPS`
- 类型: `let` (Int)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:10`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `xPointStep` 定义了 CO2 波形图 X 轴标签的采样步长，值为 `50`。
- 用于在 `stride(from: 0, through: maxXPoints, by: xPointStep)` 中生成 X 轴刻度位置。
- 当前值 50 意味着每 50 个数据点显示一个刻度标签。
- 标签文字 = `intValue / xPointStep` + 单位，即实际展示的时间步长。

## 调用链

- `LineChartView` 的 X 轴 `AxisMarks` 使用此步长生成刻度位置和标签。
- `LineChartViewForImage` 的 X 轴步长沿用此值。

## 使用建议

- 与 `maxXPoints`（400）配合，产生 8 个刻度（每 2.5 秒一个，共 20 秒窗口）。
- 减小步长会增加刻度密度，增大步长则减少密度。
<!-- context-seed:end -->
