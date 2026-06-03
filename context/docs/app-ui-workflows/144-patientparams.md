# patientParams

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L373）。
领域：导航。

## 实体定位

- 实体：patientParams
- ID / 别名：`patient`
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：Intent/路由参数名

## 补充职责

Intent/路由参数名。

## 关键 ID / 别名

`patient`

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

通过 Intent extra 或 PageScene/currentPage/currentTab 连接页面。

## 注意事项

Intent/路由参数名。

## 最小验证方式

./gradlew :app:assembleDebug；人工检查跳转和 Intent extra。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
