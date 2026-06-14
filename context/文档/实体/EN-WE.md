<!-- context-seed:start -->
# WFSpeedEnum

## 定位

- ID: `EN-WE`
- 类型: `enum` (Int)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:27`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `WFSpeedEnum` 定义了 iOS 端 CO2 波形图的时间轴滚动速度枚举。
- **case One(1)**: 慢速。
- **case Two(2)**: 中速（默认）。
- **case Four(4)**: 快速。
- `rawValue` 为 Int 类型，表示速度因子。
- 与 Android 端的 `WF_SPEED` 枚举对应。

## 使用建议

- 速度值越大，波形滚动越快。
- 在 `DisplayConfigView` 的 WFSpeed Picker 中使用。
<!-- context-seed:end -->
