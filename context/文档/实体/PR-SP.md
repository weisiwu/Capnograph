<!-- context-seed:start -->
# SuccessProtocol

## 定位

- ID: `PR-SP`
- 类型: `protocol`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/HistoryDataManage.swift:46`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SuccessProtocol` 定义了保存操作结果的成功状态接口。
- 要求遵循者提供 `var Success: Bool` 属性。
- 由 `SaveResultTypes` 遵循实现。
- 在 `HistoryDataManage.wrapLogger` 中作为泛型约束使用：`<T: SuccessProtocol>`。

## 使用建议

- 添加新的结果类型时遵循此协议以兼容 `wrapLogger` 的日志处理流程。
<!-- context-seed:end -->
