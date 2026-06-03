# CapnoEasy

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L24）。
领域：项目。
实体级上下文：`context/docs/platform-resources/001-capnoeasy.md`。

## 实体定位

- 实体：CapnoEasy
- ID / 别名：rootProject, CapnoGraph
- 源文件：`settings.gradle.kts`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：Android 二氧化碳监测应用

## 补充职责

CapnoEasy 是根 Gradle 工程，承载主应用 `:app` 和热敏打印库 `:hotmeltprint`。代码中的产品显示名常见为 CapnoGraph。

## 关键 ID / 别名

rootProject, CapnoGraph

## 关键字段 / 方法

`settings.gradle.kts` 中 `rootProject.name = "CapnoEasy"`，并 `include(":app")`、`include(":hotmeltprint")`。

## 主要调用点

Gradle wrapper、IDE 导入和模块任务都从根工程拓扑解析；页面标题/项目记忆里使用 CapnoGraph 作为显示别名。

## 注意事项

项目显示名、根工程名和应用字符串不是同一个字段；调整时需同步 settings、`R.string.app_name`、页面标题常量和实体映射。

## 最小验证方式

`./gradlew projects` 应列出根工程 `CapnoEasy` 以及 `:app`、`:hotmeltprint`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
