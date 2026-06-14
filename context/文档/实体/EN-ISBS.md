<!-- context-seed:start -->
# ISBState

## 定位

- ID: `EN-ISBS`
- 类型: `enum` (UInt8)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:153`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ISBState` 是一个组合枚举类型，聚合了所有命令状态枚举。
- **Case 分支**：`CMD_80H(ISBState80H)`、`CMD_84H(ISBState84H)`、`CMD_F2H(ISBStateF2H)`、`CMD_CAH(ISBStateCAH)`。
- 关联值枚举用于标识不同命令类型下的状态。
- 在 `getDeviceInfo()` 的回调中传递，用于区分返回值对应的命令类型。
- 与 Android 端 `CapnoEasyProtocalKit` 中的 ISBState 枚举对应。
<!-- context-seed:end -->
