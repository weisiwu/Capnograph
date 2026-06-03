# BlueToothKit.correctZero

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L290）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.correctZero`
- ID / 别名：zero command, 校零函数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：排队停止连读、发送 Zero、恢复连读，并等待状态回调

## 补充职责

发送校零命令并等待 80H 状态回调。

## 关键 ID / 别名

- 定位别名：zero command, 校零函数
- 关键字段 / 方法：`SensorCommand.Zero = 0x82`、`correctZeroCallback`、`handleCO2Status`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`SensorCommand.Zero = 0x82`、`correctZeroCallback`、`handleCO2Status`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

设置页校零动作。

## 注意事项

回调在 `ZSBState.NOZeroning` 且 `isCorrectZero` 已置位后触发。

## 最小验证方式

`rg "correctZeroCallback"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
