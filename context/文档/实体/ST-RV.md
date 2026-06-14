<!-- context-seed:start -->
# ResultView

## 定位

- ID: `ST-RV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ResultView.swift:230`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ResultView` 是 iOS 端的主页面，显示 CO2 波形实时数据和设备信息。
- 位于 `ActionsTabView` 的第二个 Tab（`PageTypes.Result`）。
- **布局**（从上到下）：
  1. `LineChartView`：实时 CO2 波形折线图（高度 300pt）。
  2. 警告横幅：根据蓝牙管理器状态显示各种警告（红色闪烁文字）：
     - 窒息警告 (`isAsphyxiation`)。
     - 低电量警告 (`isLowerEnergy`)。
     - ETCO2 越限警告（上界/下界）。
     - RR 越限警告（上界/下界）。
     - 适配器异常/污染警告。
  3. `TableView`：实时数据表格（设备名、ID、RR、ETCO2）。
  4. 顶部导航栏右侧"更多"按钮（`home_more_btn`），点击打开 `BottomSheetView`。
- 警告文字使用 1 秒间隔的闪烁动画（`isVisible` 状态配合定时器）。

## 调用链

- 在 `ActionsTabView` 中通过 `ResultView()` 创建。
- 依赖 `@EnvironmentObject bluetoothManager`、`appConfigManage`、`historyDataManage`。
- `onAppear` 时检查是否是首次展示：首次展示跳过（同步在应用启动时处理），非首次调用 `bluetoothManager.sendContinuous()`。
- 与 Android 端的 `MainActivity` 对应。

## 使用建议

- 警告优先级：窒息 > 低电量 > ETCO2 越限 > RR 越限 > 适配器异常。
- `hasAppeared` 标志确保蓝牙配置同步只执行一次。
<!-- context-seed:end -->
