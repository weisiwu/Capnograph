# BlueToothKit.getSpecificValue

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L300）。
领域：协议函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.getSpecificValue`
- ID / 别名：parse protocol frame, 解析协议帧
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：校验 NBF 和 CKS 后分派 80H、84H、CAH、F2H 处理

## 补充职责

校验帧长度和 CKS，按 CMD 分派解析函数。

## 关键 ID / 别名

- 定位别名：parse protocol frame, 解析协议帧
- 关键字段 / 方法：`calculateCKS`、`SensorCommand.CO2Waveform/Settings/GetSoftwareRevision/Expand`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`calculateCKS`、`SensorCommand.CO2Waveform/Settings/GetSoftwareRevision/Expand`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`onCharacteristicChanged`。

## 注意事项

`Zero/NACK/StopContinuous/ResetNoBreaths` 在 support list 中但没有具体分支。

## 最小验证方式

检查 when

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
