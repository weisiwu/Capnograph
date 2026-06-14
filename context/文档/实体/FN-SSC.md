<!-- context-seed:start -->
# sendStopContinuous

## 定位

- ID: `FN-SSC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:640`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `sendStopContinuous()` 是发送停止连续数据采集指令的函数。
- 向蓝牙设备发送停止指令，通知设备停止发送 CO2 波形数据。
- 与 `sendContinuous()` 成对使用，在需要暂停数据采集时调用。
<!-- context-seed:end -->
