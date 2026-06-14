<!-- context-seed:start -->
# sendContinuous

## 定位

- ID: `FN-SC`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:555`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `sendContinuous()` 是发送连续数据采集指令的函数。
- 向蓝牙设备发送 `SensorCommand.GetData` 指令，通知设备开始持续发送 CO2 波形数据。
- 发送缓存队列中的多条指令，确保设备正确启动数据采集。
- 设备回复的数据通过 `receivePeripheralData()` 接收和分发。

## 调用链

- 在设备连接并注册服务/特征值完成后调用。
- 在 `ResultView.onAppear()` 非首次出现时也调用。
- 与 Android 端 `BlueToothKit.sendContinuous()` 对应。
<!-- context-seed:end -->
