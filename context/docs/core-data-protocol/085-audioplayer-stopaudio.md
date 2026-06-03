# AudioPlayer.stopAudio

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L339）。
领域：音频函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`AudioPlayer.stopAudio`
- ID / 别名：stop alert, 停止报警音
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：停止并释放当前 MediaPlayer

## 补充职责

停止并释放 MediaPlayer。

## 关键 ID / 别名

- 定位别名：stop alert, 停止报警音
- 关键字段 / 方法：`mediaPlayer?.stop()`、`release()`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`mediaPlayer?.stop()`、`release()`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt`

## 主要调用点

报警恢复正常或播放新报警前。

## 注意事项

对未 start 的 player 调用 stop 可能有平台异常风险，当前未捕获。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
