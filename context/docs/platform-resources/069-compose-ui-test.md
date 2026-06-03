# Compose UI Test

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L474）。
领域：测试依赖。
实体级上下文：`context/docs/platform-resources/069-compose-ui-test.md`。

## 实体定位

- 实体：Compose UI Test
- ID / 别名：`ui-test-junit4`, `ui-test-manifest`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app Compose UI 测试和 debug manifest 依赖

## 补充职责

Compose UI 测试依赖和 debug test manifest。

## 关键 ID / 别名

`ui-test-junit4`, `ui-test-manifest`

## 关键字段 / 方法

`androidx.compose.ui:ui-test-junit4`、`androidx.compose.ui:ui-test-manifest`。

## 主要调用点

app androidTest/debug 声明；当前未检出 Compose UI test 文件。

## 注意事项

新增 Compose 测试需跑设备/模拟器 instrumentation。

## 最小验证方式

`./gradlew :app:connectedDebugAndroidTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
