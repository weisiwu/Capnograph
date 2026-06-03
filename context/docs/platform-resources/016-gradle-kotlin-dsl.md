# Gradle Kotlin DSL

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L49）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/016-gradle-kotlin-dsl.md`。

## 实体定位

- 实体：Gradle Kotlin DSL
- ID / 别名：`.gradle.kts`, `settings.gradle.kts`, Kotlin DSL
- 源文件：`settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：根工程和模块构建脚本均使用 Kotlin DSL

## 补充职责

项目构建脚本语言，根工程、settings、app 和 hotmeltprint 均使用 `.gradle.kts`。

## 关键 ID / 别名

`.gradle.kts`, `settings.gradle.kts`, Kotlin DSL

## 关键字段 / 方法

`settings.gradle.kts`、`build.gradle.kts`、`app/build.gradle.kts`、`hotmeltprint/build.gradle.kts`。

## 主要调用点

Gradle 配置阶段执行 Kotlin DSL；依赖别名通过 version catalog 的类型安全访问器解析。

## 注意事项

编辑 Kotlin DSL 后要跑至少 `./gradlew help`，依赖或插件变更则跑模块 assemble/dependencies。

## 最小验证方式

`./gradlew help`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
