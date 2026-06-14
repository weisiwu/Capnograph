<!-- context-seed:start -->
# BLECharacteristicUUID

## 定位

- ID: `EN-BU-02`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:62`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BLECharacteristicUUID` 定义了 BLE 设备特征值的 UUID 枚举。
- **特征值类型**：发送数据特征值、接收数据特征值、反劫持特征值、反劫持通知特征值。
- 用于 `registerCharacteristic()` 中匹配和注册特征值。
- 与 Android 端 `CapnoEasyProtocalKit.BLECharacteristicUUID` 枚举对应。
<!-- context-seed:end -->
