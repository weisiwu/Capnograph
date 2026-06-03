# BlueToothKit.searchDevices

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L283）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.searchDevices`
- ID / 别名：scan devices, 扫描设备函数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：检查蓝牙状态后启动 BLE/经典蓝牙扫描并定时停止

## 补充职责

检查蓝牙状态并按类型启动扫描，定时停止。

## 关键 ID / 别名

- 定位别名：scan devices, 扫描设备函数
- 关键字段 / 方法：`BluetoothType`、`SCAN_PERIOD`、`scanFinish`、`scanFind`、`checkBlueToothFail`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`BluetoothType`、`SCAN_PERIOD`、`scanFinish`、`scanFind`、`checkBlueToothFail`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`SearchActivity`、`MainActivity`。

## 注意事项

计时器固定 5 秒；权限/蓝牙未开会提前失败。

## 最小验证方式

`rg "fun searchDevices"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
