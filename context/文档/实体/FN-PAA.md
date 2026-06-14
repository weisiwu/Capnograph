<!-- context-seed:start -->
# playAlertAudio

## 定位

- ID: `FN-PAA`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:181`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `playAlertAudio(type: AudioType)` 是报警音频播放函数。
- 根据 `AudioType` 选择播放不同的报警提示音。
- 在 `BluetoothManager` 中定义的实例方法。
- 与 Android 端的 `AudioPlayer.play()` 对应。
<!-- context-seed:end -->
