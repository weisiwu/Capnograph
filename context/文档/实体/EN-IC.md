<!-- context-seed:start -->
# ISBStateCAH

## 定位

- ID: `EN-IC`
- 类型: `enum` (UInt8)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:144`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ISBStateCAH` 定义了蓝牙协议中 0xCA 命令状态字节的枚举。
- **状态值**：`GetProductionDate`（获取生产日期）、`GetModuleName`（获取模块名称）、`GetSoftWareRevision`（获取软件版本）。
- 用于 `getDeviceInfo()` 中查询传感器的生产信息和模块版本。
- 与 Android 端 `CapnoEasyProtocalKit.ISBStateCAH` 枚举对应。
<!-- context-seed:end -->
