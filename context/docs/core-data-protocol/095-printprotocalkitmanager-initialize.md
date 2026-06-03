# PrintProtocalKitManager.initialize

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L349）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`PrintProtocalKitManager.initialize`
- ID / 别名：printer protocol init, 打印协议初始化
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PrintProtocalKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：创建 HotmeltPinter 单例供页面使用

## 补充职责

创建 HotmeltPinter 单例。

## 关键 ID / 别名

- 定位别名：printer protocol init, 打印协议初始化
- 关键字段 / 方法：`printProtocalKit = HotmeltPinter()`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`printProtocalKit = HotmeltPinter()`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PrintProtocalKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

设计上的打印协议入口。

## 注意事项

目前主打印调用走 `BlueToothKit.gpPrinterManager`。

## 最小验证方式

`rg "PrintProtocalKitManager"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
