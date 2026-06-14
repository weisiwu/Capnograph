<!-- context-seed:start -->
# ISBState84H

## 定位

- ID: `EN-IH-02`
- 类型: `enum` (UInt8)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:116`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ISBState84H` 定义了蓝牙协议中 0x84 命令状态字节的枚举。
- **状态值**：`GetHardWareRevision`（获取硬件版本）、`GetSerialNumber`（获取序列号）、`GetSensorPartNumber`（获取传感器型号）、`GetSoftwareRevision`（获取软件版本）、`GetOEMID`（获取 OEM ID）。
- 在 `getDeviceInfo()` 查询设备信息时使用此枚举发送查询指令。
- 与 Android 端 `CapnoEasyProtocalKit.ISBState84H` 枚举对应。
<!-- context-seed:end -->
