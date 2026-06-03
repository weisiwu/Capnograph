# ETCO2Range

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L428）。
领域：常量。
聚合章节：设置与常量。

## 实体定位

- 实体：ETCO2Range
- ID / 别名：ETCO2 alert range, ETCO2 报警范围
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：当前范围：0 到 150

## 补充职责

ETCO2 报警范围 UI 边界。

## 关键 ID / 别名

- 定位别名：ETCO2 alert range, ETCO2 报警范围
- 值 / 字段：`0f..150f`

## 关键字段 / 方法

- 主要字段、方法或协议值：`0f..150f`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

报警设置。

## 注意事项

与单位换算逻辑分离。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
