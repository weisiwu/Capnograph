<!-- context-seed:start -->
# CO2_UNIT

## 定位

- ID: `AND-CU`
- 类型: `enum class` (BaseEnmu<String>)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:18`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CO2_UNIT` 定义了 Android 端支持的 CO2 浓度单位枚举。
- **MMHG("mmHg")**: 毫米汞柱。
- **KPA("kPa")**: 千帕。
- **PERCENT("%")**: 百分比。
- 遵循 `BaseEnmu<String>` 接口，与 iOS 端的 `CO2UnitType` 枚举对应。

## 调用链

- `AppState.CO2Unit` 使用此枚举存储当前单位。
- `DisplaySettingActivity` 中的 WheelPicker 使用此枚举提供单位选择。
- `EtCo2LineChart` 和 `EtCo2Table` 根据此单位格式化数据展示。
<!-- context-seed:end -->
