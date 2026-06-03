# BLECharacteristics.BLESendDataCha

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L400）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：BLECharacteristics.BLESendDataCha
- ID / 别名：`0xFFE9`
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：发送数据特征

## 补充职责

发送命令特征。

## 关键 ID / 别名

- 定位别名：`0xFFE9`
- 协议 ID / 值：`0xFFE9`

## 关键字段 / 方法

- 主要字段、方法或协议值：`0xFFE9`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

`currentSendDataCharacteristic`、`writeDataToDevice`。

## 注意事项

写入类型默认 `WRITE_TYPE_NO_RESPONSE`。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
