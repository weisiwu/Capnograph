<!-- context-seed:start -->
# InfoItem

## 定位

- ID: `ST-II`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SystemConfigView.swift:58`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `InfoItem` 是系统设置页的通用信息行组件。
- **参数**：
  - `title: String`：左侧标签文字。
  - `desc: String`：右侧描述文字（仅 `InfoTypes.Text` 模式使用）。
  - `type: InfoTypes`：展示模式（`.Text` 或 `.Radio`）。
- `.Text` 模式：固定高度 40pt 的 HStack，左侧标题（regular 16pt） + 右侧描述（thin 16pt）。
- `.Radio` 模式：渲染 `RadioButtonGroup`。

## 调用链

- 在 `SystemConfigView` 的 List 中批量使用，展示系统信息如语言、固件版本、硬件版本等。
- 不依赖额外 ViewModel，直接使用传入的 title/desc。

## 使用建议

- 添加新的系统信息条目时，在 `SystemConfigView` 的 List 中添加新的 `InfoItem`。
- 使用 `InfoTypes.Radio` 必须传入有效的 `Languages` 数组。
<!-- context-seed:end -->
