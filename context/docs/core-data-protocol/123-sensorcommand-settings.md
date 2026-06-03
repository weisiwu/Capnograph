# SensorCommand.Settings

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L389）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：SensorCommand.Settings
- ID / 别名：`0x84`, settings, 设置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：读取/修改设置指令

## 补充职责

读取/设置模块参数。

## 关键 ID / 别名

- 定位别名：`0x84`, settings, 设置
- 协议 ID / 值：`0x84`

## 关键字段 / 方法

- 主要字段、方法或协议值：`0x84`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

单位、窒息、补偿、硬件/OEM/序列号/型号/气压读取。

## 注意事项

解析入口 `handleSettings`。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
