# Page shell and overlays flow

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L109）。
领域：UI 流程。

## 实体定位

- 实体：Page shell and overlays flow
- ID / 别名：BaseLayout overlays, 全局弹层, 页面框架
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/BaseActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/BaseLayout.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：所有 Activity 共享顶栏、底栏、Toast、Alert、Confirm、Loading、ActionModal

## 补充职责

所有 Activity 共享顶栏、底栏、Toast、Alert、Confirm、Loading、ActionModal。

## 关键 ID / 别名

BaseLayout overlays, 全局弹层, 页面框架

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/BaseActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/BaseLayout.kt`

## 主要调用点

按源文件和定位表反查调用点。

## 注意事项

所有 Activity 共享顶栏、底栏、Toast、Alert、Confirm、Loading、ActionModal。

## 最小验证方式

./gradlew :app:assembleDebug

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
