<!-- context-seed:start -->
# CO2UnitType

## 定位

- ID: `EN-CUT`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:9`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CO2UnitType` 定义了 iOS 端支持的 CO2 浓度单位枚举。
- **case KPa**: 千帕（rawValue "KPa"）。
- **case Percentage**: 百分比（rawValue "%"）。
- **case mmHg**: 毫米汞柱（rawValue "mmHg"），为默认单位。
- 与 Android 端的 `CO2_UNIT` 枚举对应。

## 调用链

- `AppConfigManage.CO2Unit` 和 `BluetoothManager.CO2Unit` 存储当前单位。
- `DisplayConfigView` 中的 Picker 使用此枚举提供单位选择。
- `LineChartView` 和 `TableView` 根据单位格式化数据展示。
- 单位变化时联动更新 `CO2Scale` 的可用选项。
<!-- context-seed:end -->
