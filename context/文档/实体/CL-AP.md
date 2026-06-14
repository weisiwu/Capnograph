<!-- context-seed:start -->
# AudioPlayer

## 定位

- ID: `CL-AP`
- 类型: `class`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:172`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AudioPlayer` 是 iOS 端的报警音频播放类。
- 使用 `AVAudioPlayer` 播放系统报警音。
- 提供 `playAlertAudio(type:)` 和 `stopAudio()` 接口。
- 在 `BluetoothManager` 中创建和使用，当设备报警状态触发时播放对应音频。

## 调用链

- 在 `BluetoothManager` 中作为属性持有，`BluetoothManager.playAlertAudio()` / `stopAudio()` 调用。
- 与 Android 端的 `AudioPlayer`（AlertAudioKit 中）对应。
<!-- context-seed:end -->
