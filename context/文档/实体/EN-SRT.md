<!-- context-seed:start -->
# SaveResultTypes

## 定位

- ID: `EN-SRT`
- 类型: `enum` (SuccessProtocol)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:51`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SaveResultTypes` 定义了历史数据导出操作的结果枚举，遵循 `SuccessProtocol`。
- **case Success**: 成功状态。
- **case Error(SaveErrorTypes)**: 失败状态，携带具体错误类型。
- `Success` 属性始终返回 `true`（注意：`Error` case 因其当前实现也返回 `true`，有逻辑问题）。

## 调用链

- `_savePDFToLocal()` 返回 `.Success` 或 `.Error(.xxx)`。
- `wrapLogger` 通过 `as? SaveResultTypes` 进行类型检查并打印日志。

## 使用建议

- 注意 `Success` 在 `Error` case 上也返回 `true`，这是已知代码问题（实际使用时通过 switch 分支处理）。
- 添加新结果类型时确保 `SuccessProtocol` 正确实现。
<!-- context-seed:end -->
