# hotmeltprint unit test

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L513）。
领域：测试。
实体级上下文：`context/docs/platform-resources/093-hotmeltprint-unit-test.md`。

## 实体定位

- 实体：hotmeltprint unit test
- ID / 别名：ExampleUnitTest, hotmeltprint 单元测试
- 源文件：`hotmeltprint/src/test/java/com/wldmedical/hotmeltprint/ExampleUnitTest.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：hotmeltprint JVM 测试占位文件

## 补充职责

hotmeltprint JVM 单元测试入口。

## 关键 ID / 别名

ExampleUnitTest, hotmeltprint 单元测试

## 关键字段 / 方法

`hotmeltprint/src/test/java/com/wldmedical/hotmeltprint/ExampleUnitTest.kt`。

## 主要调用点

当前 `addition_isCorrect` 断言 `2 + 2 = 4`，作为占位测试。

## 注意事项

新增打印协议纯函数测试可放在此处；GPrinter/蓝牙仍需要设备集成验证。

## 最小验证方式

`./gradlew :hotmeltprint:testDebugUnitTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
