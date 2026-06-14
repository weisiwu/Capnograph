<!-- context-seed:start -->
# HistoryData

## 定位

- ID: `CL-HD`
- 类型: `class`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:81`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `HistoryData` 是波形历史数据类，记录一次波形采集的完整数据及元信息。
- **核心属性**：
  - `recordStartDate` / `recordStartDateStr`: 记录起始时间和格式化字符串。
  - `recordEndDate` / `recordEndDateStr`: 记录结束时间（懒加载，导出时自动获取）。
  - `minRR` / `maxRR` / `minETCO2` / `maxETCO2`: 绘图辅助的极值。
  - `CO2WavePoints: [CO2WavePointData]`: 波形数据点列表。
- **方法**：
  - `updateEndRecordDate(end:)`: 设置结束时间并格式化。
  - `markEndTime()`: 标记当前时间为结束时间。

## 调用链

- 由 `HistoryDataManage.syncBluetoothManagerData()` 创建。
- 数据来自 `BluetoothManager.totalCO2WavedData`。
- 在 `LineChartViewForImage` 中被绘制为 PDF 图表。

## 使用建议

- 生命周期从开始录制到导出完成，一次采集对应一个 `HistoryData` 实例。
- 结束时间在导出 PDF 时确定（`saveToLocal()` 中调用 `updateEndRecordDate`）。
<!-- context-seed:end -->
