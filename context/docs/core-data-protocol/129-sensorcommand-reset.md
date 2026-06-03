# SensorCommand.Reset

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L395）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：SensorCommand.Reset
- ID / 别名：`0xF8`, reset, 复位
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：复位指令

## 补充职责

复位/关机命令。

## 关键 ID / 别名

- 定位别名：`0xF8`, reset, 复位
- 协议 ID / 值：`0xF8`

## 关键字段 / 方法

- 主要字段、方法或协议值：`0xF8`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

`shutdown`。

## 注意事项

不在 `supportCMDs`，当前只发送。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
