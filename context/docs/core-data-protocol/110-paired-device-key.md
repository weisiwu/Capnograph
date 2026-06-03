# PAIRED_DEVICE_KEY

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L369）。
领域：存储。
聚合章节：存储与数据库 ID。

## 实体定位

- 实体：PAIRED_DEVICE_KEY
- ID / 别名：`paired_device_address`
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：已配对设备地址 key

## 补充职责

已配对 CapnoEasy 地址 key。

## 关键 ID / 别名

- 定位别名：`paired_device_address`
- 关键字段 / 方法：`paired_device_address`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`paired_device_address`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

`savePairedBLEDevice`、`getSavedBLEDeviceAddress`。

## 注意事项

只保存一个地址。

## 最小验证方式

`rg "PAIRED_DEVICE_KEY"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
