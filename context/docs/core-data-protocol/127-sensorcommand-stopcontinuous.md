# SensorCommand.StopContinuous

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L393）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：SensorCommand.StopContinuous
- ID / 别名：`0xC9`, stop continuous, 停止连读
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：停止连续读取模式

## 补充职责

停止连续读取。

## 关键 ID / 别名

- 定位别名：`0xC9`, stop continuous, 停止连读
- 协议 ID / 值：`0xC9`

## 关键字段 / 方法

- 主要字段、方法或协议值：`0xC9`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

设置/校零/复位前。

## 注意事项

当前不单独处理响应。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
