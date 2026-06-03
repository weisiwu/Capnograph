# BluetoothTaskQueue.addTask

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L311）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BluetoothTaskQueue.addTask`
- ID / 别名：enqueue task, 添加单任务
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将单个 Runnable 加入串行队列

## 补充职责

加入单个 Runnable。

## 关键 ID / 别名

- 定位别名：enqueue task, 添加单任务
- 关键字段 / 方法：`taskQueue.offer`、`ReentrantLock`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`taskQueue.offer`、`ReentrantLock`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`

## 主要调用点

蓝牙命令构造处。

## 注意事项

只入队不执行。

## 最小验证方式

检查调用后是否执行 `executeTask`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
