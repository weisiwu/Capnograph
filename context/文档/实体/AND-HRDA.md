<!-- context-seed:start -->
# HistoryRecordDetailActivity

## 定位

- ID: `AND-HRDA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt:63`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `HistoryRecordDetailActivity` 是 Android 端的历史记录详情页（`PageScene.HISTORY_DETAIL_PAGE`）。
- **核心功能**：
  - `loadAllCo2Data()`: 从数据库加载完整的 CO2 波形数据。
  - `createPdfDocument()`: 创建 PDF 报告文档。
  - `savePdfToUri()`: 将 PDF 保存到指定 Uri。
  - `onSavePDFClick()`: 导出 PDF 按钮处理逻辑。
  - `onPrintTicketClick()`: 打印小票按钮处理逻辑。
- `Content()` 渲染波形图（`EtCo2LineChart`）和详细信息。

## 调用链

- 从 `HistoryRecordsActivity` 点击记录进入。
- 与 iOS 端的 `LineChartViewForImage` + `HistoryDataManage` 对应。
<!-- context-seed:end -->
