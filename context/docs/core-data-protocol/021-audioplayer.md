# AudioPlayer

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L231）。
领域：音频。
聚合章节：Kit 与服务。

## 实体定位

- 实体：AudioPlayer
- ID / 别名：audio player, 音频播放器
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：MediaPlayer 包装类

## 补充职责

MediaPlayer 包装类。

## 关键 ID / 别名

- 定位别名：audio player, 音频播放器
- 关键字段 / 方法：`playAlertAudio`、`stopAudio`、`isPlay`、`playStatus`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`playAlertAudio`、`stopAudio`、`isPlay`、`playStatus`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`

## 主要调用点

`BlueToothKit` 的 `audioIns`。

## 注意事项

`stopAudio` 不重置 `isPlay`；14 秒延迟回调会重置。

## 最小验证方式

`rg "playAlertAudio|stopAudio"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
