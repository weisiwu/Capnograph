# AlertAudioKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L230）。
领域：音频。
聚合章节：Kit 与服务。

## 实体定位

- 实体：AlertAudioKit
- ID / 别名：alert sound, 报警音频
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：报警声音播放

## 补充职责

报警音频能力集合。

## 关键 ID / 别名

- 定位别名：alert sound, 报警音频
- 关键字段 / 方法：`AudioPlayer`、`AlertAudioType`、`R.raw.low_level_alert`、`R.raw.middle_level_alert`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`AudioPlayer`、`AlertAudioType`、`R.raw.low_level_alert`、`R.raw.middle_level_alert`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`

## 主要调用点

`BlueToothKit.handleCO2Waveform`。

## 注意事项

播放时把 music 音量调到最大音量 30%，14 秒后自动停止；播放、停止或释放异常会通过 ErrorReporter 上报。

## 最小验证方式

检查 `AlertAudioKit.kt`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
