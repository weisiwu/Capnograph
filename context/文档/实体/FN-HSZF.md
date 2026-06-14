<!-- context-seed:start -->
# handleSetZeroFail

## 定位

- ID: `FN-HSZF`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:138`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleSetZeroFail` 是 iOS 端校零失败的回调处理函数。
- 清空 loading 消息，显示失败 Toast（type: `.FAIL`）。
- 与 `handleSetZero` 配合使用，在校零指令发送失败时被调用。
<!-- context-seed:end -->
