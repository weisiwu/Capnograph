<!-- context-seed:start -->
# AudioPlayer

## 定位

- ID: `AND-AP`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt:11`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AudioPlayer` 是 Android 端的报警音频播放类。
- 使用 `MediaPlayer` 播放报警提示音。
- 根据 `AlertAudioType` 枚举播放不同类型的报警音。

## 调用链

- 在 `BlueToothKit` 中创建，用于设备报警状态时发出声音。
- 与 iOS 端的 `AudioPlayer`（BuetoothManage 中的类）对应。
<!-- context-seed:end -->
