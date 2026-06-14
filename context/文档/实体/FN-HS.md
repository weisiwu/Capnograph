<!-- context-seed:start -->
# handleSettings

## 定位

- ID: `FN-HS`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1037`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleSettings(data: [UInt8])` 是蓝牙设置参数响应处理函数。
- 解析设备返回的 0xE2 类型设置参数响应帧。
- 更新当前的 ETCO2 报警范围、RR 报警范围、窒息时间、氧气补偿等参数。
- 与 Android 端对应的参数响应解析逻辑对应。
<!-- context-seed:end -->
