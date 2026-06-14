<!-- context-seed:start -->
# getSpecificValue

## 定位

- ID: `FN-GSV`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:893`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `getSpecificValue(data: [UInt8], startBit: Int, length: Int) -> Int` 是蓝牙数据帧解析工具函数。
- 从原始字节数据中提取指定起始位和长度的数值。
- 用于解析蓝牙协议中各参数字段的位置。
- 在 `handleCO2Waveform` 和 `handleCO2Status` 中调用。
<!-- context-seed:end -->
