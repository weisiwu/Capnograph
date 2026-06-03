# BluetoothTaskQueue.executeAllTasks

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L314）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BluetoothTaskQueue.executeAllTasks`
- ID / 别名：drain queue, 执行全部任务
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：依次执行队列直到为空

## 补充职责

异步清空任务队列。

## 关键 ID / 别名

- 定位别名：drain queue, 执行全部任务
- 关键字段 / 方法：while `taskQueue.isNotEmpty()`。

## 关键字段 / 方法

- 主要字段、方法或协议值：while `taskQueue.isNotEmpty()`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`

## 主要调用点

断开连接/写失败后的后续处理。

## 注意事项

失败 3 次后丢弃当前任务继续。

## 最小验证方式

检查 loop

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
