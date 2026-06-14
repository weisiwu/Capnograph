<!-- context-seed:start -->
# WF_SPEED

## 定位

- ID: `AND-WF`
- 类型: `enum class` (BaseEnmu<Float>)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:42`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `WF_SPEED` 定义了 CO2 波形图的时间轴速度（每秒显示多少秒数据）。
- **SMALL(1f) / MIDDLE(2f) / LARGE(4f)**: 慢/中/快三档速度。
- 与 iOS 端的 `WFSpeedEnum` 对应。
- 提供预配置 `wfSpeeds` 列表和 `wfSpeedsObj` 选择器配置。

## 调用链

- `AppState.WFSpeed` 存储当前速度。
- `EtCo2LineChart` 根据此值调整 X 轴滚动速度。
<!-- context-seed:end -->
