<!-- context-seed:start -->
# ConfigItem

## 定位

- ID: `ST-CI`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:45`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ConfigItem` 是设置页菜单项的单个行组件。
- **参数**：`titleKey: String`（多语言 key）、`configType: ConfigItemTypes`（类型）、`action: () -> Void`（点击回调）。
- 渲染布局：左侧标题 + 右侧箭头图标（`setting_right_arrow`）。
- 在 `ConfigView` 的 List 中被多个实例化，形成设置菜单列表。

## 调用链

- 由 `ConfigView` 批量创建，对应设置页的各个菜单项。
- 点击后通过 `configType` 分发到对应的子页面。
<!-- context-seed:end -->
