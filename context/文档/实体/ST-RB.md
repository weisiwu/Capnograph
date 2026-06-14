<!-- context-seed:start -->
# RadioButton

## 定位

- ID: `ST-RB`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SystemConfigView.swift:13`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `RadioButton` 是一个单选按钮组件，通过 `Languages` 枚举绑定选中状态。
- 展示逻辑：选中时显示 `radio_input`（蓝色填充），未选中时显示 `radio_input_empty`（灰色空心）。
- 点击触发 `appConfigManage.language = id`，改变全局语言设置。
- 按钮布局为水平 HStack：左侧图标 + 右侧语言名称文本。

## 调用链

- 被 `RadioButtonGroup` 批量创建，用于 `SystemConfigView` 的语言切换。
- 通过 `EnvironmentObject appConfigManage` 监听和应用语言变化。

## 使用建议

- 语言切换后需要重新渲染当前页面，目前通过 `@EnvironmentObject` 的自动更新触发。
- `InfoItem` 中的 `Radio` 类型分支负责包裹 RadioButtonGroup。
<!-- context-seed:end -->
