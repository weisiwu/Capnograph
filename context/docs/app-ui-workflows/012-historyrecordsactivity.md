# HistoryRecordsActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L86）。
领域：页面。

## 实体定位

- 实体：HistoryRecordsActivity
- ID / 别名：HISTORY_LIST_PAGE, 历史记录
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordsActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：按患者/日期分组的记录列表

## 补充职责

历史列表页，从 RecordDao Flow 读取记录，按全部/患者/日期分组交给 HistoryList 展示。

## 关键 ID / 别名

HISTORY_LIST_PAGE, 历史记录

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordsActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

按患者/日期分组的记录列表。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
