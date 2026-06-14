<!-- context-seed:start -->
# SingleSlider

## 定位

- ID: `ST-SSLDR`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ModuleConfigView.swift:59`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SingleSlider` 是一个带标题和实时数值标签的滑块组件。
- **参数**：
  - `title: String`：配置项标题。
  - `minimumValue: Float` / `maximumValue: Float`：滑块范围。
  - `unit: String = "S"`：数值单位后缀。
  - `value: Binding<Double>`：绑定的当前值（双向）。
- 布局：顶部标题 → 中间实时数值标签（带单位，动画跟随滑块位置）→ 底部 `SingleSliderInner`。
- 数值标签位置通过 `calculateOffsetX` 动态计算，确保始终对齐在滑块上方。
- 滑块强调色为青色 `Color(red: 0, green: 206/255, blue: 201/255)`。

## 调用链

- 在 `ModuleConfigView` 中使用：第一个用于窒息时间（`"S"` 单位），第二个用于氧气补偿（`"%"` 单位）。
- 数值格式化为 1 位小数。

## 使用建议

- 适用于范围型参数的配置入口。
- 数值标签的偏移计算依赖 GeometryReader 获取父容器的宽度。
<!-- context-seed:end -->
