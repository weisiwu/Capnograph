<!-- context-seed:start -->
# InfoTypes

## 定位

- ID: `EN-IT`
- 类型: `enum`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SystemConfigView.swift:52`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `InfoTypes` 定义了 `InfoItem` 的展示模式。
- **case Text**: 文本模式，左侧标题 + 右侧描述文字。
- **case Radio**: 单选按钮模式，左侧标题 + 右侧 `RadioButtonGroup`。

## 使用建议

- 用于 `InfoItem` 的 `type` 参数，控制信息的展现形式。
- 目前仅在 `SystemConfigView` 中使用。
<!-- context-seed:end -->
