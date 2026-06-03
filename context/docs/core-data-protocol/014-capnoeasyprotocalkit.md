# CapnoEasyProtocalKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L224）。
领域：协议。
聚合章节：Kit 与服务。

## 实体定位

- 实体：CapnoEasyProtocalKit
- ID / 别名：protocol, 协议, UUID, 指令
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：BLE 服务/特征 UUID 和传感器指令

## 补充职责

协议 ID 和 UUID 定义。

## 关键 ID / 别名

- 定位别名：protocol, 协议, UUID, 指令
- 关键字段 / 方法：`SensorCommand`、`BLEServers`、`BLECharacteristics`、`ISBState*`、`supportCMDs`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`SensorCommand`、`BLEServers`、`BLECharacteristics`、`ISBState*`、`supportCMDs`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

`BlueToothKit` 构造命令和解析响应。

## 注意事项

`supportCMDs` 不包含 `SensorCommand.Reset`，因为 Reset 当前只作为发送命令。

## 最小验证方式

`rg "supportCMDs"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
