<!-- context-seed:start -->
# SaveChartToPdfTask

## 定位

- ID: `AND-PDF`
- 类型: `class` (AsyncTask)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SaveChartToPdfTask` 是 Android 端的 PDF 导出类，继承 `AsyncTask`。
- **核心流程**：
  1. 在后台加载 CO2 波形数据。
  2. 使用 Android Canvas / PDF API 渲染波形图到 PDF。
  3. 添加页眉（设备信息、时间）和水印。
  4. 保存到本地文件系统。
- 支持分页：每页展示固定数量的数据点。

## 调用链

- 在 `HistoryRecordDetailActivity.onSavePDFClick()` 中调用。
- 与 iOS 端的 `HistoryDataManage._savePDFToLocal()` 对应。
<!-- context-seed:end -->
