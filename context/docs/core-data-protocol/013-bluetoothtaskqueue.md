# BluetoothTaskQueue

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L223）。
领域：蓝牙。
聚合章节：Kit 与服务。

## 实体定位

- 实体：BluetoothTaskQueue
- ID / 别名：BLE write queue, 写队列
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：串行化 BLE 任务/写操作

## 补充职责

串行执行 BLE 写入任务，避免多条命令同时写特征。

## 关键 ID / 别名

- 定位别名：BLE write queue, 写队列
- 关键字段 / 方法：`addTask`、`addTasks`、`executeTask`、`executeAllTasks`、`taskQueueSize`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`addTask`、`addTasks`、`executeTask`、`executeAllTasks`、`taskQueueSize`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`

## 主要调用点

`BlueToothKit` 初始化、设置、校零、复位等命令链。

## 注意事项

异步线程内失败最多重试 3 次；执行失败和重试失败会通过 ErrorReporter 上报并携带队列长度/重试次数；队列锁只保护取队列过程。

## 最小验证方式

检查 `BlueToothTaskQueueKit.kt`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
