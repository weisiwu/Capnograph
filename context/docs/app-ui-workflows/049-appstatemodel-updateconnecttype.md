# AppStateModel.updateConnectType

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L153）。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updateConnectType`
- ID / 别名：connect type state, 蓝牙连接类型
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/TypeSwitch.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：更新 BLE/经典蓝牙类型选择

## 补充职责

更新 BLE/经典蓝牙类型选择。

## 关键 ID / 别名

connect type state, 蓝牙连接类型

## 关键字段 / 方法

- 主要实体或方法：`AppStateModel.updateConnectType`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/TypeSwitch.kt`

## 主要调用点

由页面、组件、蓝牙回调或本地存储回读调用，用于写入 AppState。

## 注意事项

更新 BLE/经典蓝牙类型选择。

## 最小验证方式

./gradlew :app:assembleDebug；rg 函数名确认调用点和状态副作用。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
