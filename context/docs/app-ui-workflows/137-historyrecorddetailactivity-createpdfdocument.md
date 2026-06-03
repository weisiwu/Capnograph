# HistoryRecordDetailActivity.createPdfDocument

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L279）。
领域：页面函数。

## 实体定位

- 实体：`HistoryRecordDetailActivity.createPdfDocument`
- ID / 别名：ACTION_CREATE_DOCUMENT, 创建 PDF 文档
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：调用系统文档创建器选择 PDF 保存位置

## 补充职责

调用系统文档创建器选择 PDF 保存位置。

## 关键 ID / 别名

ACTION_CREATE_DOCUMENT, 创建 PDF 文档

## 关键字段 / 方法

- 主要实体或方法：`HistoryRecordDetailActivity.createPdfDocument`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt`

## 主要调用点

由对应 Activity 生命周期、导航栏按钮、保存按钮或 Compose 内容回调触发。

## 注意事项

调用系统文档创建器选择 PDF 保存位置。

## 最小验证方式

./gradlew :app:assembleDebug；人工触发对应按钮/生命周期路径。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
