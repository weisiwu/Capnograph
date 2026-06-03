# PrintProtocalKitManager

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L229）。
领域：打印。
聚合章节：Kit 与服务。

## 实体定位

- 实体：PrintProtocalKitManager
- ID / 别名：print protocol, 打印协议
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PrintProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：热敏打印指令/协议辅助对象

## 补充职责

app 模块里的热敏打印 SDK 单例入口。

## 关键 ID / 别名

- 定位别名：print protocol, 打印协议
- 关键字段 / 方法：`printProtocalKit: HotmeltPinter`、`initialize()`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`printProtocalKit: HotmeltPinter`、`initialize()`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PrintProtocalKit.kt`

## 主要调用点

设计上供页面使用；当前蓝牙模块另有 `gpPrinterManager = HotmeltPinter()`。

## 注意事项

存在两个 HotmeltPinter 持有路径，调用点以 `BlueToothKit.gpPrinterManager` 为主。

## 最小验证方式

`rg "PrintProtocalKitManager|gpPrinterManager"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
