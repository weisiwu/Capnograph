# BlueToothKit.innerUpdateAlertRange

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L295）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.innerUpdateAlertRange`
- ID / 别名：build alert packet, 构造报警指令
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将报警上下限编码为 7-bit 拆分数据并发送

## 补充职责

将报警上下限拆成 7-bit 数据并发送。

## 关键 ID / 别名

- 定位别名：build alert packet, 构造报警指令
- 关键字段 / 方法：`Expand 0xF2`、NBF `0x0A`、ISB `0x2A`、ETCO2 值乘 10。

## 关键字段 / 方法

- 主要字段、方法或协议值：`Expand 0xF2`、NBF `0x0A`、ISB `0x2A`、ETCO2 值乘 10。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`updateAlertRange`、初始化默认报警设置。

## 注意事项

co2 和 rr 上下限都小于等于 0 时不发送。

## 最小验证方式

检查 `shr 7` 与 `and 0x7f`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
