<!-- context-seed:start -->
# checkBluetoothStatus

## 定位

- ID: `FN-CBS`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1402`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `checkBluetoothStatus() -> Bool?` 是蓝牙状态检查函数。
- 检查当前蓝牙中心管理器的状态，返回是否已就绪。
- 在设置页面执行校零、关机、更新参数等操作前调用，确保蓝牙已连接。
- 与 Android 端 `BaseActivity.checkBluetoothPermissions()` 中的状态检查对应。
<!-- context-seed:end -->
