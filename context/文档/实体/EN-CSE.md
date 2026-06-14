<!-- context-seed:start -->
# CO2ScaleEnum

## 定位

- ID: `EN-CSE`
- 类型: `enum` (Float)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:15`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CO2ScaleEnum` 定义了 iOS 端 CO2 波形图 Y 轴刻度的枚举。
- **mmHg 系列**: `mmHg_Small(50)` / `mmHg_Middle(60)` / `mmHg_Large(75)`。
- **KPa 系列**: `KPa_Small(6.7)` / `KPa_Middle(8)` / `KPa_Large(10)`。
- **百分比系列**: `percentage_Small(6.6)` / `percentage_Middle(7.9)` / `percentage_Large(9.9)`。
- `rawValue` 为 Float 类型，表示 Y 轴最大值。
- 与 Android 端的 `CO2_SCALE` 枚举对应。

## 调用链

- `BluetoothManager.CO2Scale` 存储当前刻度。
- `LineChartView` 的 `chartYScale(domain:)` 根据此值设置 Y 轴范围。
- `DisplayConfigView` 中单位变化时，`CO2Scales` 列表联动切换。
- `generateYAxis()` 函数根据此枚举生成 Y 轴刻度标签数组。
<!-- context-seed:end -->
