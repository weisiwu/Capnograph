# GROUP_BY

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L424）。
领域：存储。
聚合章节：设置与常量。

## 实体定位

- 实体：GROUP_BY
- ID / 别名：ALL, PATIENT, DATE, 记录分组
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：历史记录分组枚举

## 补充职责

历史记录分组方式。

## 关键 ID / 别名

- 定位别名：ALL, PATIENT, DATE, 记录分组
- 值 / 字段：`ALL`, `PATIENT`, `DATE`

## 关键字段 / 方法

- 主要字段、方法或协议值：`ALL`, `PATIENT`, `DATE`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`LocalStorageKit.state`、历史列表。

## 注意事项

本任务只记录枚举，历史列表 UI 见其他 context。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
