# BlueToothKit.initCapnoEasyConection

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L285）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.initCapnoEasyConection`
- ID / 别名：init device commands, 初始化设备连接
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：注册发送特征后排队发送停止连读、设置、设备信息、连续读取等命令

## 补充职责

注册发送特征，排队下发初始化/读取命令。

## 关键 ID / 别名

- 定位别名：init device commands, 初始化设备连接
- 关键字段 / 方法：`getSystemInfo`、`sendStopContinuous`、`updateCO2Unit`、`updateCO2Scale`、`updateNoBreath`、`innerUpdateAlertRange`、`sendContinuous`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`getSystemInfo`、`sendStopContinuous`、`updateCO2Unit`、`updateCO2Scale`、`updateNoBreath`、`innerUpdateAlertRange`、`sendContinuous`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`

## 主要调用点

`onServicesDiscovered`。

## 注意事项

默认初始化不会立即 `executeTask()`，依赖写回调/后续执行；`getSystemInfo=true` 分支会执行。

## 最小验证方式

检查函数末尾

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
