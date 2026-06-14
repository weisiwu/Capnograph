<!-- context-seed:start -->
# maxXPoints

## 定位

- ID: `VA-MXP`
- 类型: `let` (Int)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:8`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `maxXPoints` 定义了 CO2 波形图表横坐标的最大点数，值为 `400`。
- 配合 `xPointStep`（50）使用，展示约 20 秒的波形数据（400/50 = 8 个刻度，每个刻度 2.5 秒）。
- 每个数据点对应约 10ms 的采集间隔（蓝牙数据帧接收间隔）。

## 调用链

- 在 `LineChartView` 的 X 轴 `AxisMarks` 中作为 `stride(from: 0, through: maxXPoints, by: xPointStep)` 的上界。

## 使用建议

- 增加 `maxXPoints` 会扩展波形图表的时间窗口，但增加渲染开销。
- 需与 `xPointStep` 配合调整以保持合适的刻度和步长。
<!-- context-seed:end -->
