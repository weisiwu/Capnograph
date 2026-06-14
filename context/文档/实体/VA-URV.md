<!-- context-seed:start -->
# unRealValue

## 定位

- ID: `VA-URV`
- 类型: `let` (Float)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:11`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `unRealValue` 定义了波形数据点的初始默认值，值为 `0`。
- 用于初始化波形数据数组中的所有点位初始值。
- 值为 0 表示无有效数据，图表渲染时通过 `max(point.value, 0)` 确保不绘制负值。

## 使用建议

- 如果使用不同的默认值（如 -1 区分无效数据），修改此常量并使图表渲染逻辑适配。
<!-- context-seed:end -->
