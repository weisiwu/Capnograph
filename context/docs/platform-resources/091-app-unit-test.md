# app unit test

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L511）。
领域：测试。
实体级上下文：`context/docs/platform-resources/091-app-unit-test.md`。

## 实体定位

- 实体：app unit test
- ID / 别名：ExampleUnitTest, app 单元测试
- 源文件：`app/src/test/java/com/wldmedical/capnoeasy/ExampleUnitTest.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app JVM 测试占位文件

## 补充职责

app JVM 单元测试入口。

## 关键 ID / 别名

ExampleUnitTest, app 单元测试

## 关键字段 / 方法

`app/src/test/java/com/wldmedical/capnoeasy/ExampleUnitTest.kt`；测试类 `ExampleUnitTest`。

## 主要调用点

当前 `addition_isCorrect` 断言 `2 + 2 = 4`，作为占位测试。

## 注意事项

新增 app 纯 JVM 业务测试放在 `app/src/test`，避免依赖 Android runtime。

## 最小验证方式

`./gradlew :app:testDebugUnitTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
