# BlueToothKit.updateCO2Unit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L292）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateCO2Unit`
- ID / 别名：set CO2 unit, 设置单位
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：发送 Settings/SetCO2Unit 指令

## 补充职责

构造 Settings/SetCO2Unit 命令。

## 关键 ID / 别名

- 定位别名：set CO2 unit, 设置单位
- 关键字段 / 方法：`Settings 0x84`、NBF `0x03`、`ISBState84H.SetCO2Unit = 7`、`MMHG=0`、`KPA=1`、`PERCENT=2`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`Settings 0x84`、NBF `0x03`、`ISBState84H.SetCO2Unit = 7`、`MMHG=0`、`KPA=1`、`PERCENT=2`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

`updateCO2UnitScale`、初始化命令。

## 注意事项

传 null 不发送。

## 最小验证方式

检查分支映射

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
