<!-- context-seed:start -->
# centralManagerDidUpdateState

## 定位

- ID: `FN-CMDUS`
- 类型: `function` (CBCentralManagerDelegate)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1308`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `centralManagerDidUpdateState(_ central: CBCentralManager)` 是实现 `CBCentralManagerDelegate` 协议的中心管理器状态更新回调。
- 在蓝牙硬件状态变化时（打开/关闭/未授权）被系统调用。
- 根据 `CBManagerState` 状态自动启动或停止蓝牙扫描。
- 与 Android 端 `BlueToothKit` 的蓝牙权限检查逻辑对应。
<!-- context-seed:end -->
