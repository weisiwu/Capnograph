# BluetoothTaskQueue.addTasks

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L312）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BluetoothTaskQueue.addTasks`
- ID / 别名：enqueue tasks, 添加多任务
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将多个 Runnable 批量加入串行队列

## 补充职责

批量加入 Runnable。

## 关键 ID / 别名

- 定位别名：enqueue tasks, 添加多任务
- 关键字段 / 方法：`taskQueue.addAll(tasks)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`taskQueue.addAll(tasks)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`

## 主要调用点

初始化、设置、校零、复位。

## 注意事项

包含调试 `println`。

## 最小验证方式

`rg "addTasks"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
