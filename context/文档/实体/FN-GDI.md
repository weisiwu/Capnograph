<!-- context-seed:start -->
# getDeviceInfo

## 定位

- ID: `FN-GDI`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:594`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `getDeviceInfo(cb: @escaping (String, ISBState) -> Void)` 是获取设备信息的函数。
- **查询的信息**：硬件版本、序列号、传感器型号、软件版本、OEM ID、生产日期、模块名称。
- 通过轮询方式发送多条查询指令（每条指令间间隔 100ms）。
- 回调 `cb` 在每条信息获取成功后返回，包含值字符串和对应的 `ISBState` 类型。
- 在 `SystemConfigView` 进入时自动调用以填充设备信息显示。

## 调用链

- 在 `SystemConfigView.onAppear()` 中调用。
- 与 Android 端 `BlueToothKit.fetchDeviceInfo()` 对应。
<!-- context-seed:end -->
