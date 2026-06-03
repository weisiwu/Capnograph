# JUnit 4

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L471）。
领域：测试依赖。
实体级上下文：`context/docs/platform-resources/066-junit-4.md`。

## 实体定位

- 实体：JUnit 4
- ID / 别名：`junit:junit:4.13.2`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：JVM 单元测试依赖

## 补充职责

JVM 单元测试框架。

## 关键 ID / 别名

`junit:junit:4.13.2`

## 关键字段 / 方法

`junit:junit:4.13.2`。

## 主要调用点

app 和 hotmeltprint 的 `ExampleUnitTest` 使用 `@Test` 与 `assertEquals(4, 2 + 2)`。

## 注意事项

当前只是占位单测；新增业务测试需放到对应 module 的 `src/test`。

## 最小验证方式

`./gradlew :app:testDebugUnitTest :hotmeltprint:testDebugUnitTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
