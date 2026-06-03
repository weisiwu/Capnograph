# Alarm evaluation flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L110）。
领域：报警。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Alarm evaluation flow
- ID / 别名：alert audio, 报警判断, 报警音
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`, `app/src/main/res/raw/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：波形解析时根据 ETCO2/RR、窒息、低电量、校零和适配器状态触发报警音

## 补充职责

根据生理/技术报警条件播放中级或低级报警音。

## 关键 ID / 别名

- 定位别名：alert audio, 报警判断, 报警音
- 关键字段 / 方法：`isAsphyxiation`、`isLowerEnergy`、`isNeedZeroCorrect`、`isAdaptorInvalid`、`isAdaptorPolluted`、`AudioPlayer.playAlertAudio`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`isAsphyxiation`、`isLowerEnergy`、`isNeedZeroCorrect`、`isAdaptorInvalid`、`isAdaptorPolluted`、`AudioPlayer.playAlertAudio`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`, `app/src/main/res/raw/`

## 主要调用点

`BlueToothKit.handleCO2Waveform` 和 `handleCO2Status`。

## 注意事项

中级报警：窒息、ETCO2/RR 异常、低电量；低级报警：需校零、无适配器、适配器污染。

## 最小验证方式

检查 `AlertAudioType` 和 `R.raw.*_alert`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
