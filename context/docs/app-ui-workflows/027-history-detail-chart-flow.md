# History detail chart flow

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L117）。
领域：历史记录。

## 实体定位

- 实体：History detail chart flow
- ID / 别名：record detail chart, 历史详情图表
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：详情页分页读取 CO2Data chunk，渲染趋势图和波形图

## 补充职责

详情页分页读取 CO2Data chunk，渲染趋势图和波形图。

## 关键 ID / 别名

record detail chart, 历史详情图表

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

由历史列表页、详情页和 LocalStorageKit/Room 查询共同使用。

## 注意事项

详情页分页读取 CO2Data chunk，渲染趋势图和波形图。

## 最小验证方式

./gradlew :app:assembleDebug；人工进入历史列表和详情页。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
