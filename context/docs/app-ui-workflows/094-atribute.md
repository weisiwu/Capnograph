# Atribute

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L208）。
领域：组件模型。

## 实体定位

- 实体：Atribute
- ID / 别名：attribute row, 表格属性行
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2Table.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：EtCo2Table 患者/实时指标行配置；类名当前拼写为 `Atribute`

## 补充职责

EtCo2Table 患者/实时指标行配置；类名当前拼写为 `Atribute`。

## 关键 ID / 别名

attribute row, 表格属性行

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2Table.kt`

## 主要调用点

作为组件入参或 UI 状态载体使用，字段变化会影响调用方构造方式。

## 注意事项

EtCo2Table 患者/实时指标行配置；类名当前拼写为 `Atribute`。

## 最小验证方式

./gradlew :app:assembleDebug；rg 调用点确认构造参数同步。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
