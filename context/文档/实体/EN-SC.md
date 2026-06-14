<!-- context-seed:start -->
# SensorCommand

## 定位

- ID: `EN-SC`
- 类型: `enum` (UInt8)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:27`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SensorCommand` 定义了 iOS 端与 CapnoGraph 传感器通信的指令码枚举。
- **主要指令**：`GetData(0xE1)` 获取数据、`Settings(0xE2)` 设置参数、`GetSoftwareRevision(0xE5)` 获取软件版本、`Control(0xE6)` 控制指令。
- 每个枚举值的 rawValue 为 UInt8 类型，对应蓝牙协议中的指令字节。
- 与 Android 端 `CapnoEasyProtocalKit.SensorCommand` 枚举对应。

## 使用建议

- 修改协议时保持与 Android 端的 `SensorCommand` 枚举同步。
<!-- context-seed:end -->
