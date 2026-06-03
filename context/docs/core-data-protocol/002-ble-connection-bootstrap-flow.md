# BLE connection bootstrap flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L108）。
领域：蓝牙流程。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：BLE connection bootstrap flow
- ID / 别名：connect device, GATT, 反劫持, 初始化连接
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：连接设备、发现服务/特征、写入反劫持、订阅通知并发送初始化命令

## 补充职责

连接 CapnoEasy，发现 GATT 服务/特征，写入反劫持，订阅通知并下发初始化命令。

## 关键 ID / 别名

- 定位别名：connect device, GATT, 反劫持, 初始化连接
- 关键字段 / 方法：`connectDevice`、`gattCallback`、`antiHijackData = "301001301001"`、`sortedBLEServersUUID`、`BLECharacteristicUUID.BLEReceiveDataCha`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`connectDevice`、`gattCallback`、`antiHijackData = "301001301001"`、`sortedBLEServersUUID`、`BLECharacteristicUUID.BLEReceiveDataCha`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt`

## 主要调用点

`SearchActivity.onDeviceClick`、搜索结束自动连接历史设备、`MainActivity.onCreate`。

## 注意事项

BLE 或 unknown 类型走 `connectGatt`；classic 分支当前 `connectClassicDevice` 为空。

## 最小验证方式

检查 `onServicesDiscovered` 和 `initCapnoEasyConection`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
