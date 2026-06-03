# BlueToothKit.fetchDeviceInfo

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L286）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.fetchDeviceInfo`
- ID / 别名：poll device info, 轮询设备信息
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：周期查询硬件版本、OEM、序列号、型号和软件版本直到齐全

## 补充职责

周期查询设备硬件、OEM、序列号、型号、软件版本直到齐全。

## 关键 ID / 别名

- 定位别名：poll device info, 轮询设备信息
- 关键字段 / 方法：`sendPeriod = 100L`、`checkJob`、`ISBState84H.Get*`、`SensorCommand.GetSoftwareRevision`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`sendPeriod = 100L`、`checkJob`、`ISBState84H.Get*`、`SensorCommand.GetSoftwareRevision`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`initCapnoEasyConection`。

## 注意事项

每个字段为空才排队查询；全部非空后跳出。

## 最小验证方式

`rg "fetchDeviceInfo"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
