# RangeType

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L425）。
领域：组件。
聚合章节：设置与常量。

## 实体定位

- 实体：RangeType
- ID / 别名：range selector mode, 范围选择模式
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/RangeSelector.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：范围选择模式枚举

## 补充职责

范围选择器模式。

## 关键 ID / 别名

- 定位别名：range selector mode, 范围选择模式
- 值 / 字段：`BOTH`, `ONESIDE`

## 关键字段 / 方法

- 主要字段、方法或协议值：`BOTH`, `ONESIDE`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/components/RangeSelector.kt`

## 主要调用点

报警范围、模块参数、历史详情滑块。

## 注意事项

`ONESIDE` 单值，`BOTH` 起止值。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
