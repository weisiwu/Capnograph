# BlueToothKit.updateNoBreath

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L297）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.updateNoBreath`
- ID / 别名：set no breath, 设置窒息时间
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：发送 Settings/NoBreaths 指令

## 补充职责

构造 Settings/NoBreaths 命令。

## 关键 ID / 别名

- 定位别名：set no breath, 设置窒息时间
- 关键字段 / 方法：`ISBState84H.NoBreaths = 6`、NBF `0x03`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`ISBState84H.NoBreaths = 6`、NBF `0x03`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

模块设置保存、初始化命令。

## 注意事项

直接把 Int 转 Byte。

## 最小验证方式

检查函数体

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
