<!-- context-seed:start -->
# EtCo2LineChart

## 定位

- ID: `AND-ELC`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt:62`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `EtCo2LineChart` 是 Android 端主页的实时 CO2 波形折线图组件。
- 使用 MPAndroidChart 库绘制。
- Y 轴范围根据 `CO2_SCALE` 动态变化。
- X 轴根据 `WF_SPEED` 调整滚动速度。
- 数据从 `AppStateModel` 中实时更新。

## 调用链

- 在 `MainActivity.Content()` 中作为主页顶部核心组件。
- 与 iOS 端的 `LineChartView` 对应。
<!-- context-seed:end -->
