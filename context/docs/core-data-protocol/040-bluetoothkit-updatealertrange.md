# BlueToothKit.updateAlertRange

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L294）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateAlertRange`
- ID / 别名：set alert range, 设置报警范围
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：停止连读后发送 ETCO2/RR 报警上下限并恢复连读

## 补充职责

停止连读后发送 ETCO2/RR 报警上下限并恢复连读。

## 关键 ID / 别名

- 定位别名：set alert range, 设置报警范围
- 关键字段 / 方法：`co2Low`、`co2Up`、`rrLow`、`rrUp`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`co2Low`、`co2Up`、`rrLow`、`rrUp`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

报警设置保存。

## 注意事项

实际编码在 `innerUpdateAlertRange`。

## 最小验证方式

`rg "updateAlertRange"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
