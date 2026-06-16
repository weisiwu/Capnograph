# BLE search flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L107）。
领域：蓝牙流程。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：BLE search flow
- ID / 别名：search devices, 扫描设备, 附近设备
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/SearchActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：搜索页触发 BLE/经典蓝牙扫描、设备列表更新和连接动作

## 补充职责

搜索 BLE 与经典蓝牙设备，并把扫描结果送入 UI 状态。

## 关键 ID / 别名

- 定位别名：search devices, 扫描设备, 附近设备
- 关键字段 / 方法：`BlueToothKit.searchDevices`、`scanBleDevices`、`scanClassicDevices`、`BluetoothType.ALL/BLE/CLASSIC`、`SCAN_PERIOD = 5000`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`BlueToothKit.searchDevices`、`scanBleDevices`、`scanClassicDevices`、`BluetoothType.ALL/BLE/CLASSIC`、`SCAN_PERIOD = 5000`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/SearchActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`SearchActivity.Content` 的 `DeviceList.onSearch`；`MainActivity.onCreate` 默认以 `BluetoothType.CLASSIC` 扫描打印机。

## 注意事项

搜索失败走 `checkBlueToothFail`；BLE `onScanFailed` 会通过 ErrorReporter 上报 errorCode；经典扫描只自动连接 GP 打印机，不向 `discoveredPeripherals` 列表追加普通设备。

## 最小验证方式

`rg "searchDevices\\("`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
