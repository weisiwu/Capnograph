# AudioPlayer.playAlertAudio

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L338）。
领域：音频函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`AudioPlayer.playAlertAudio`
- ID / 别名：play alert, 播放报警音
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`, `app/src/main/res/raw/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：根据报警级别播放低/中级报警音

## 补充职责

根据级别播放报警音。

## 关键 ID / 别名

- 定位别名：play alert, 播放报警音
- 关键字段 / 方法：`LowLevelAlert`、`MiddleLevelAlert`、`R.raw.low_level_alert`、`R.raw.middle_level_alert`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`LowLevelAlert`、`MiddleLevelAlert`、`R.raw.low_level_alert`、`R.raw.middle_level_alert`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`, `app/src/main/res/raw/`

## 主要调用点

`BlueToothKit.handleCO2Waveform`。

## 注意事项

正在播放时直接返回；14 秒后停止。

## 最小验证方式

检查 Handler 延迟

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
