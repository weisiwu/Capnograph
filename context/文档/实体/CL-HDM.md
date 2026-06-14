<!-- context-seed:start -->
# HistoryDataManage

## 定位

- ID: `CL-HDM`
- 类型: `class` (ObservableObject)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:332`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `HistoryDataManage` 是历史数据的管理类，负责波形数据的导出（当前仅支持 PDF）。
- 继承 `ObservableObject`，通过 `@Published var pdfURL: URL?` 通知 UI 导出结果。
- **核心工作流**：
  1. `listenToBluetoothManager(bluetoothManager:)`: 绑定蓝牙管理器实例。
  2. `syncBluetoothManagerData()`: 同步蓝牙中的波形数据到 `HistoryData`。
  3. `saveToLocal(args:)`: 外部统一调用入口，先同步数据 → 更新结束时间 → 调用保存逻辑。
  4. `_saveToLocal(type:)`: 根据保存类型分发（switch）。
  5. `_savePDFToLocal()`: 实际的 PDF 生成逻辑，使用 `ImageRenderer` 渲染 `LineChartViewForImage`。
  6. `wrapLogger(function:args:)`: 统一错误日志处理，catch 各种 `SaveErrorTypes`。

## 调用链

- 作为 `@EnvironmentObject` 注入到 `BottomSheetView` 和 `ResultView`。
- `BottomSheetView` 通过 `historyDataManage.saveToLocal()` 触发导出。
- 导出完成后 `pdfURL` 被设置，`ShareLink` 使用该 URL 提供分享。

## 使用建议

- PDF 文件名格式：`{recordStartDateStr}-{recordEndDateStr}.pdf`。
- 每页展示 500 个数据点，通过 `chunked(into:)` 分页。
<!-- context-seed:end -->
