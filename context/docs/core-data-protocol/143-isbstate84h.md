# ISBState84H

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L409）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：ISBState84H
- ID / 别名：84H settings fields, 84H 设置字段
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：大气压、温度、窒息时间、单位、补偿、序列号、硬件版本和停止采样

## 补充职责

84H 设置/读取字段。

## 关键 ID / 别名

- 定位别名：84H settings fields, 84H 设置字段
- 协议 ID / 值：`1`, `4`, `5`, `6`, `7`, `8`, `9`, `11`, `18`, `19`, `20`, `21`, `27`

## 关键字段 / 方法

- 主要字段、方法或协议值：`1`, `4`, `5`, `6`, `7`, `8`, `9`, `11`, `18`, `19`, `20`, `21`, `27`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

设备初始化、设置页保存、`handleSettings`。

## 注意事项

`NoUse = 0`；`Stop = 27` 当前无调用。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
