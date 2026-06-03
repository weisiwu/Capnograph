# SensorCommand.ResetNoBreaths

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L394）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：SensorCommand.ResetNoBreaths
- ID / 别名：`0xCC`, reset no breaths, 清除窒息状态
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：清除窒息/无呼吸状态

## 补充职责

清除无呼吸/窒息状态。

## 关键 ID / 别名

- 定位别名：`0xCC`, reset no breaths, 清除窒息状态
- 协议 ID / 值：`0xCC`

## 关键字段 / 方法

- 主要字段、方法或协议值：`0xCC`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

`supportCMDs`。

## 注意事项

当前没有发送调用点。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
