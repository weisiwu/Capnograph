# BlueToothKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L221）。
领域：蓝牙。
聚合章节：Kit 与服务。

## 实体定位

- 实体：BlueToothKit
- ID / 别名：BLE核心, 蓝牙, 扫描, 连接, GATT
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：BLE 和经典蓝牙操作、反劫持、数据流

## 补充职责

蓝牙核心服务，负责扫描、连接、GATT、命令队列、实时数据流和设备状态。

## 关键 ID / 别名

- 定位别名：BLE核心, 蓝牙, 扫描, 连接, GATT
- 关键字段 / 方法：`bluetoothAdapter`、`currentGatt`、`sendArray`、`receivedArray`、`dataFlow`、`currentETCO2`、`currentRespiratoryRate`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`bluetoothAdapter`、`currentGatt`、`sendArray`、`receivedArray`、`dataFlow`、`currentETCO2`、`currentRespiratoryRate`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`BaseActivity.initializeBlueToothKit` 初始化，页面通过 `BlueToothKitManager.blueToothKit` 使用。

## 注意事项

同时持有 CapnoEasy 与 GP 打印机状态；classic CapnoEasy 连接未实现。

## 最小验证方式

`rg "class BlueToothKit"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
