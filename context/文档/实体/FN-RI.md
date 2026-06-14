<!-- context-seed:start -->
# resetInstance

## 定位

- ID: `FN-RI`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1248`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `resetInstance()` 是蓝牙管理器实例重置函数。
- 清空所有蓝牙连接状态、设备列表和缓存数据。
- 在设备断开或应用需要完全重置蓝牙状态时调用。
- 与 Android 端 `BlueToothKit.resetInstance()` 对应。
<!-- context-seed:end -->
