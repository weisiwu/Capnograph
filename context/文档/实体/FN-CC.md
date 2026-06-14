<!-- context-seed:start -->
# calculateCKS

## 定位

- ID: `FN-CC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:527`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `calculateCKS(data: [UInt8]) -> [UInt8]` 是蓝牙协议校验和计算函数。
- 对数据数组进行累加和校验，将校验值追加到数组末尾。
- 在每个发送给设备的指令包中调用，确保数据完整性。
- 与 Android 端的校验和计算逻辑对应。
<!-- context-seed:end -->
