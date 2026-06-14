<!-- context-seed:start -->
# convertToData

## 定位

- ID: `FN-CTD`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:550`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `convertToData(data: [UInt8]) -> Data` 是将字节数组转换为 `Data` 对象的函数。
- 用于将蓝牙协议指令数组转换为 `Foundation.Data` 类型后发送给设备。
- 在 `writeDataToDevice()` 发送指令时调用。
<!-- context-seed:end -->
