<!-- context-seed:start -->
# BLEServerUUID

## 定位

- ID: `EN-BU`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:42`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BLEServerUUID` 定义了 BLE 设备服务的 UUID 枚举。
- **服务类型**：发送数据服务、接收数据服务、反劫持服务、模块参数服务。
- 每个 case 的 rawValue 为 16 进制 UUID 字符串。
- 用于 `registerService()` 中匹配和注册发现的 BLE 服务。
- 与 Android 端 `CapnoEasyProtocalKit.BLEServersUUID` 枚举对应。
<!-- context-seed:end -->
