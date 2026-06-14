<!-- context-seed:start -->
# CustomType

## 定位

- ID: `AND-CT`
- 类型: `interface`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/TypeSwitch.kt:33`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CustomType` 是自定义类型的通用接口。
- **属性**：`name: String`（显示名称）、`id: Int`（标识）、`index: Int`（索引）。
- 被 `DeviceType` 和 `SupportQRCodeType` 实现，用于 TypeSwitch 等选择器组件。
<!-- context-seed:end -->
