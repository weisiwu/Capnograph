# AlertAudioType

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L423）。
领域：音频。
聚合章节：设置与常量。

## 实体定位

- 实体：AlertAudioType
- ID / 别名：low/middle alert, 报警声音
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：报警音频类型枚举

## 补充职责

报警音级别。

## 关键 ID / 别名

- 定位别名：low/middle alert, 报警声音
- 值 / 字段：`LowLevelAlert`, `MiddleLevelAlert`

## 关键字段 / 方法

- 主要字段、方法或协议值：`LowLevelAlert`, `MiddleLevelAlert`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`

## 主要调用点

`AudioPlayer.playAlertAudio`。

## 注意事项

低级对应技术报警，中级对应生理/低电量报警。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
