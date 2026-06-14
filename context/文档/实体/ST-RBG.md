<!-- context-seed:start -->
# RadioButtonGroup

## 定位

- ID: `ST-RBG`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SystemConfigView.swift:3`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `RadioButtonGroup` 是一个单选按钮组容器，接收 `items: [Languages]` 数组。
- 内部遍历 items，为每个语言选项创建一个 `RadioButton`。
- 仅用于系统设置页的语言选择区域。

## 调用链

- 在 `SystemConfigView` 的 `InfoItem(type: InfoTypes.Radio)` 的 `InfoTypes.Radio` 分支中被调用。
- 直接与 `Languages` 枚举（`.Chinese` / `.English`）绑定。

## 使用建议

- 如需更多语言选项，扩展 `Languages` 枚举并传入新数组即可。
- 布局方式为垂直堆叠，`padding(.leading, 20)`。
<!-- context-seed:end -->
