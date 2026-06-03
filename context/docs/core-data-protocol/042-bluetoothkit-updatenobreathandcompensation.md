# BlueToothKit.updateNoBreathAndCompensation

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L296）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateNoBreathAndCompensation`
- ID / 别名：set module params, 设置模块参数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：排队更新窒息时间和 O2 补偿

## 补充职责

排队更新窒息时间与 O2 补偿。

## 关键 ID / 别名

- 定位别名：set module params, 设置模块参数
- 关键字段 / 方法：`updateNoBreath`、`updateGasCompensation`、`sendStopContinuous`、`sendContinuous`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`updateNoBreath`、`updateGasCompensation`、`sendStopContinuous`、`sendContinuous`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

模块设置保存、初始化命令。

## 注意事项

回调在恢复连读后执行。

## 最小验证方式

检查任务列表顺序

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
