<!-- context-seed:start -->
# BLEDeviceExtra

## 定位

- ID: `AND-BDE`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt:66`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BLEDeviceExtra` 是蓝牙设备的扩展属性类，存储每个 BLE 设备的 GATT 服务和特征值引用。
- **属性**：
  - `gatt`: 当前设备的 BluetoothGatt 连接。
  - `sendDataService` / `sendDataCharacteristic`: 发送数据通道。
  - `receiveDataService` / `receiveDataCharacteristic`: 接收数据通道。
  - `antiHijackService` / `antiHijackCharacteristic` / `antiHijackNotifyCharacteristic`: 反劫持通道。
  - `moduleParamsService`: 模块参数服务。
  - `receivedArray`: 接收数据缓冲区（BlockingQueue）。
  - `taskQueue`: 蓝牙指令任务队列。
- 实例通过 `BlueToothKit` 中的 `BluetoothDevice.extra` 扩展属性访问，使用 `WeakHashMap` 自动管理内存。

## 使用建议

- `WeakHashMap` 在设备断开连接或被回收时自动清理外部实例。
<!-- context-seed:end -->
