# Gradle wrapper

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L510）。
领域：构建。
实体级上下文：`context/docs/platform-resources/090-gradle-wrapper.md`。

## 实体定位

- 实体：Gradle wrapper
- ID / 别名：`./gradlew`, `gradle-8.10.2-all`, Gradle 包装器
- 源文件：`gradlew`, `gradle/wrapper/gradle-wrapper.properties`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：本地构建/测试入口

## 补充职责

构建与测试命令入口，所有任务列表中的最小验证命令都应优先使用项目 wrapper。

## 关键 ID / 别名

`./gradlew`, `gradle-8.10.2-all`, Gradle 包装器

## 关键字段 / 方法

`./gradlew`、`gradlew.bat`、`gradle/wrapper/gradle-wrapper.jar`、`gradle-wrapper.properties`；发行包为 Gradle 8.10.2 all。

## 主要调用点

开发机、CI 或 Android Studio 同步可通过 wrapper 执行一致版本的 Gradle。

## 注意事项

这是定位表 L510 的构建入口实体；L48 的 `Gradle wrapper` 已在 `015-gradle-wrapper.md` 记录构建平台语境。

## 最小验证方式

`./gradlew --version`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
