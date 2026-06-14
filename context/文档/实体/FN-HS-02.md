<!-- context-seed:start -->
# handleShutdown

## 定位

- ID: `FN-HS-02`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ConfigView.swift:120`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleShutdown` 是 iOS 端的传感器关机处理函数。
- 流程：检查蓝牙连接 → 调用 `bluetoothManager.shutdown()` 发送关机指令 → 设置 loading 消息 → 成功后清空 loading 并显示完成 Toast。
- 失败时显示失败 Toast（带 FAIL 类型）。
- 在 `BaseConfigContainerView` 的"关机"按钮中调用。
<!-- context-seed:end -->
