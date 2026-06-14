<!-- context-seed:start -->
# appendCKS

## 定位

- ID: `FN-AC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:542`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `appendCKS(data: [UInt8]) -> [UInt8]` 是校验和追加函数。
- 调用 `calculateCKS()` 计算校验值后追加到数据包末尾。
- 用于构建完整的蓝牙指令帧。
<!-- context-seed:end -->
