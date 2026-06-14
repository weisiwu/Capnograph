<!-- context-seed:start -->
# resetSendData

## 定位

- ID: `FN-RSD`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:936`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `resetSendData()` 是重置发送数据缓存的函数。
- 清空当前待发送的指令缓存数组。
- 在发送新指令前调用，确保指令队列不混入旧数据。
<!-- context-seed:end -->
