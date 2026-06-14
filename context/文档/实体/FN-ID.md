<!-- context-seed:start -->
# initDevice

## 定位

- ID: `FN-ID`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1126`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `initDevice()` 是设备初始化函数。
- 设备连接成功后发送初始化配置序列（报警范围、显示参数、指令序列等）。
- 确保设备参数与本地配置同步。
- 与 Android 端 `BlueToothKit.initCapnoEasyConection()` 对应。
<!-- context-seed:end -->
