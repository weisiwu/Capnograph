<!-- context-seed:start -->
# getCMDDataArray

## 定位

- ID: `FN-GCA`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:853`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `getCMDDataArray() -> [UInt8]` 是获取当前指令缓存数组的函数。
- 返回待发送的指令数据缓存（`sendArray`）。
- 用于构建和发送蓝牙指令帧前获取完整的数据包。
<!-- context-seed:end -->
