# HistoryRecordDetailActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L87）。
领域：页面。

## 实体定位

- 实体：HistoryRecordDetailActivity
- ID / 别名：HISTORY_DETAIL_PAGE, 记录详情, PDF导出, 打印详情
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：记录详情、图表预览、PDF 导出和打印

## 补充职责

历史详情页，根据 recordId 加载 Record 和 CO2Data chunk，渲染趋势图/波形图，并提供 PDF 导出或热敏打印。

## 关键 ID / 别名

HISTORY_DETAIL_PAGE, 记录详情, PDF导出, 打印详情

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

记录详情、图表预览、PDF 导出和打印。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
