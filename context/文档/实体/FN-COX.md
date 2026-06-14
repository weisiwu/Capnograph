<!-- context-seed:start -->
# calculateOffsetX

## 定位

- ID: `FN-COX`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AlertConfigView.swift:39`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `calculateOffsetX(for value: CGFloat, in geometry: GeometryProxy, with valueRange: CGFloat, minV minimumValue: Float) -> CGFloat` 是滑块偏移量计算函数。
- 根据当前值、范围总宽、最小值和 GeometryProxy 计算滑块上方数值标签的偏移位置。
- 确保数值标签始终对齐在滑块按钮上方。
- 在 `RangeSlider` 和 `SingleSlider` 中使用。
<!-- context-seed:end -->
