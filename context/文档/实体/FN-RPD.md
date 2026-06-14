<!-- context-seed:start -->
# receivePeripheralData

## 定位

- ID: `FN-RPD`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:500`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `receivePeripheralData(data: NSData?)` 是蓝牙数据接收函数。
- 在 `peripheral(_:didUpdateValueFor:error:)` 委托回调中调用。
- 将接收到的原始字节数据解析并存入 `receivedArray` 缓冲区。
- 根据首字节协议类型分发到不同的数据处理函数：
  - 0x80 类型 → `handleCO2Status()`
  - 0xE1 类型 → `handleCO2Waveform()`
  - 0xE2 类型 → `handleSettings()`
  - 0xE5 类型 → `handleSofrWareVersion()`
  - 其他 → `handleSystemExpand()`

## 调用链

- BLE 特征值数据到达时自动触发。
- 与 Android 端 `BlueToothKit.gattCallback.onCharacteristicChanged()` 中的数据处理逻辑对应。
<!-- context-seed:end -->
