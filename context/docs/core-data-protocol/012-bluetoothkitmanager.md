# BlueToothKitManager

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L222）。
领域：蓝牙。
聚合章节：Kit 与服务。

## 实体定位

- 实体：BlueToothKitManager
- ID / 别名：bluetooth manager singleton, 蓝牙管理单例
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：蓝牙 Kit 访问 Manager 对象

## 补充职责

持有全局蓝牙 Kit 实例。

## 关键 ID / 别名

- 定位别名：bluetooth manager singleton, 蓝牙管理单例
- 关键字段 / 方法：`lateinit var blueToothKit`、`initialize(activity, appState, reInit)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`lateinit var blueToothKit`、`initialize(activity, appState, reInit)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`BaseActivity`。

## 注意事项

`reInit = true` 会重建实例；否则已初始化时直接返回。

## 最小验证方式

`rg "BlueToothKitManager.initialize"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
