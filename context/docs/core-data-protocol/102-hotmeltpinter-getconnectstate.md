# HotmeltPinter.getConnectState

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L356）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`HotmeltPinter.getConnectState`
- ID / 别名：printer connection state, 热敏打印连接状态
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：对外暴露 Printer 连接状态

## 补充职责

对外暴露 Printer 连接状态。

## 关键 ID / 别名

- 定位别名：printer connection state, 热敏打印连接状态
- 关键字段 / 方法：`printer.getConnectState()`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`printer.getConnectState()`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

详情页打印前。

## 注意事项

与 `Printer.getConnectState` 同源。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
