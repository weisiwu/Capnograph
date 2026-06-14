<!-- context-seed:start -->
# Array.chunked

## 定位

- ID: `EX-ARRAY-CHK`
- 类型: `extension` (Array)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:258`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `Array.chunked(into size: Int)` 是一个数组分块扩展方法。
- 将任意数组按指定大小分割为多个子数组，返回 `[[Element]]`。
- 使用 `stride(from:to:by:)` 遍历，`Swift.min` 处理最后一个不完整块。

## 调用链

- 在 `HistoryDataManage._savePDFToLocal()` 中用于将 `totalCO2WavedData` 分割为每块 500 个数据点，用于 PDF 分页渲染。

## 使用建议

- `size` 参数传 0 会陷入无限循环，确保传入正整数。
<!-- context-seed:end -->
