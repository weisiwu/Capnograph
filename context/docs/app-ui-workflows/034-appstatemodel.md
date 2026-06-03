# AppStateModel

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L133）。
领域：状态。

## 实体定位

- 实体：AppStateModel
- ID / 别名：ViewModel, HiltViewModel
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：当前项目唯一 ViewModel

## 补充职责

唯一 HiltViewModel，暴露 AppState 的 Compose State/StateFlow，并提供 update* 函数作为页面和组件的状态写入口。

## 关键 ID / 别名

ViewModel, HiltViewModel

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`

## 主要调用点

通过 Hilt 注入或 AppStateModel 暴露给页面/组件。

## 注意事项

当前项目唯一 ViewModel。

## 最小验证方式

./gradlew :app:assembleDebug；rg update*/State 调用点确认读写一致。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
