<!-- context-seed:start -->
# BluetoothTaskQueue

## 定位

- ID: `AND-BTQ`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt:8`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BluetoothTaskQueue` 是 Android 端的蓝牙指令任务队列。
- 使用 FIFO 队列管理需要发送给设备的蓝牙指令。
- 确保指令按顺序发送，避免并发冲突。
- 在 `BLEDeviceExtra.taskQueue` 中使用。
<!-- context-seed:end -->
