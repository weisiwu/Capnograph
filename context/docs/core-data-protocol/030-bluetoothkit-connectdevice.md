# BlueToothKit.connectDevice

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L284）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.connectDevice`
- ID / 别名：connect CapnoEasy, 连接设备函数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：根据 BluetoothDevice 类型选择 BLE 或经典蓝牙连接

## 补充职责

根据设备类型连接 CapnoEasy。

## 关键 ID / 别名

- 定位别名：connect CapnoEasy, 连接设备函数
- 关键字段 / 方法：`DEVICE_TYPE_LE`、`DEVICE_TYPE_UNKNOWN`、`DEVICE_TYPE_CLASSIC`、`onDeviceConnectSuccess`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`DEVICE_TYPE_LE`、`DEVICE_TYPE_UNKNOWN`、`DEVICE_TYPE_CLASSIC`、`onDeviceConnectSuccess`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

搜索页点击、自动连接历史地址。

## 注意事项

classic 分支当前空实现但仍会设置 `connectedCapnoEasy`。

## 最小验证方式

检查 `connectClassicDevice`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
