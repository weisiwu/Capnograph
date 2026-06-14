<!-- context-seed:start -->
# CO2_SCALE

## 定位

- ID: `AND-CS`
- 类型: `enum class` (BaseEnmu<Float>)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:27`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CO2_SCALE` 定义了 Android 端 CO2 波形图的 Y 轴刻度范围。
- **SMALL(50f) / MIDDLE(60f) / LARGE(75f)**: mmHg 单位的三种刻度。
- **KPA_SMALL(6.7f) / KPA_MIDDLE(8f) / KPA_LARGE(10f)**: kPa 单位的三种刻度。
- **PERCENT_SMALL(6.6f) / PERCENT_MIDDLE(7.9f) / PERCENT_LARGE(9.9f)**: 百分比的三种刻度。
- 与 iOS 端的 `CO2ScaleEnum` 一一对应。

## 调用链

- `AppState.CO2Scale` 存储当前刻度。
- `EtCo2LineChart` 的 Y 轴范围根据此枚举动态设定。
- 单位变化时，`co2Scales` 列表联动切换。
<!-- context-seed:end -->
