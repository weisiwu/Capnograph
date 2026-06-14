<!-- context-seed:start -->
# handleSetZero

## 定位

- ID: `FN-HSZ`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:130`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleSetZero` 是 iOS 端的 CO2 传感器校零处理函数。
- 流程：检查蓝牙连接 → 调用 `bluetoothManager.correctZero()` 发送校零指令 → 设置 loading → 成功后清空 loading 并显示完成 Toast。
- 失败时调用 `handleSetZeroFail`。
- 在 `BaseConfigContainerView` 的"校零"按钮中调用。
<!-- context-seed:end -->
