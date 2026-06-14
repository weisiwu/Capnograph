<!-- context-seed:start -->
# SaveErrorTypes

## 定位

- ID: `EN-SET`
- 类型: `enum` (Error)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:20`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SaveErrorTypes` 定义了历史数据导出过程中的所有错误类型，遵循 `Error` 协议。
- **case NoData**: 无保存数据（`totalCO2WavedData` 为空或 data/blm 为 nil）。
- **case InvalidDirectory**: 无法获取文档目录（`FileManager.default.urls` 失败）。
- **case UnSupportFileType**: 不支持的导出类型（非 `.PDF` 类型传入时抛出）。
- **case SaveFailed**: 保存操作通用失败。

## 调用链

- 在 `_savePDFToLocal()` 中可能抛出 `.InvalidDirectory`、`.NoData`。
- 在 `_saveToLocal(type:)` 的 `default` 分支中抛出 `.UnSupportFileType`。
- 在 `wrapLogger` 中通过 `do-catch` 捕获并记录日志。

## 使用建议

- 添加新的导出格式时同步增加对应的 Error case。
- 所有保存操作应使用 `wrapLogger` 统一处理错误日志。
<!-- context-seed:end -->
