# BlueToothKit.sendSavedData

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L288）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.sendSavedData`
- ID / 别名：send command packet, 发送命令包
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：追加 CKS 后写入当前发送特征并清空发送队列

## 补充职责

对 `sendArray` 追加 CKS 并写入当前发送特征。

## 关键 ID / 别名

- 定位别名：send command packet, 发送命令包
- 关键字段 / 方法：`appendCKS`、`writeDataToDevice`、`resetSendData`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`appendCKS`、`writeDataToDevice`、`resetSendData`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

所有设备命令构造函数。

## 注意事项

GATT 或特征为空时直接清空发送数组。

## 最小验证方式

`rg "sendSavedData\\("`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
