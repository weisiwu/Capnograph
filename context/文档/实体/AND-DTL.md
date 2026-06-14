<!-- context-seed:start -->
# DeviceTypeList

## 定位

- ID: `AND-DTL`
- 类型: `enum class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/TypeSwitch.kt:45`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `DeviceTypeList` 定义了支持的连接类型枚举。
- **枚举值**：`BLE`（低功耗蓝牙）、`WIFI`（WiFi 连接）、`USB`（USB 连接）。
- 每个枚举值封装一个 `DeviceType` 实例。
- 提供 `DeviceTypes` 常量数组供选择器使用。
<!-- context-seed:end -->
