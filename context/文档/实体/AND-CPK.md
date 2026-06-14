<!-- context-seed:start -->
# CapnoEasyProtocalKit

## 定位

- ID: `AND-CPK`
- 类型: `class / enums`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CapnoEasyProtocalKit` 定义了 Android 端与 CapnoGraph 硬件设备通信的蓝牙协议。
- **核心枚举**：
  - `SensorCommand`: 传感器指令类型（`GetData`、`Settings` 等）。
  - `ZSBState`: 数据包起始状态。
  - `ISBState84H`: 0x84 命令下的设备信息枚举。
  - `ISBStateCAH`: 0xCA 命令下的设备信息枚举。
  - `BLEServersUUID`: BLE 服务 UUID 列表。
  - `BLECharacteristicUUID`: BLE 特征值 UUID 列表。
  - `BLEDescriptorUUID`: BLE 描述符 UUID 列表。
- 与 iOS 端 `BluetoothManage.swift` 中的同名枚举完全对应。

## 使用建议

- 修改协议时同时更新 iOS 端对应枚举以保持双端一致。
<!-- context-seed:end -->
