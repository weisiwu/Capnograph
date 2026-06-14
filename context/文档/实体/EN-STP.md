<!-- context-seed:start -->
# SaveTypes

## 定位

- ID: `EN-STP`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:15`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SaveTypes` 定义了历史数据支持的导出文件类型。
- **case PDF = "pdf"**: 导出为 PDF 格式（当前唯一支持的导出格式）。
- `rawValue` 用于生成文件后缀名。

## 调用链

- `HistoryDataManage.saveToLocal()` 默认使用 `.PDF` 类型。
- 在 `_saveToLocal(type:)` 中通过 switch 分发到具体的保存逻辑。
- PDF 文件名格式：`{recordStartDateStr}-{recordEndDateStr}.pdf`。

## 使用建议

- 如需支持更多导出格式（如 CSV、图像），添加新的 case 并实现对应保存函数。
<!-- context-seed:end -->
