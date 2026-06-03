# Kotlin kapt

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L57）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/024-kotlin-kapt.md`。

## 实体定位

- 实体：Kotlin kapt
- ID / 别名：`kotlin-kapt`, kapt, annotation processing
- 源文件：`app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Hilt 和 Room 注解处理使用 kapt

## 补充职责

app 模块注解处理入口，用于 Hilt 和 Room compiler。

## 关键 ID / 别名

`kotlin-kapt`, kapt, annotation processing

## 关键字段 / 方法

`id("kotlin-kapt")`；`kapt.arguments` 设置 `room.schemaLocation=$projectDir/schemas`；底部 `kapt { correctErrorTypes = true }`。

## 主要调用点

处理 `@HiltAndroidApp`、`@HiltViewModel`、Room `@Database`/DAO/entity 等注解。

## 注意事项

Hilt/Room 相关编译失败通常出现在 kapt 任务，不一定等到 assemble。

## 最小验证方式

`./gradlew :app:kaptDebugKotlin`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
