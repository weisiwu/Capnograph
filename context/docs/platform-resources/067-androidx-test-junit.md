# AndroidX Test JUnit

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L472）。
领域：测试依赖。
实体级上下文：`context/docs/platform-resources/067-androidx-test-junit.md`。

## 实体定位

- 实体：AndroidX Test JUnit
- ID / 别名：`androidx.test.ext:junit:1.2.1`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Android instrumentation JUnit 扩展

## 补充职责

Android instrumentation JUnit 扩展。

## 关键 ID / 别名

`androidx.test.ext:junit:1.2.1`

## 关键字段 / 方法

`androidx.test.ext:junit:1.2.1`。

## 主要调用点

两个模块的 `ExampleInstrumentedTest` 使用 `AndroidJUnit4` runner。

## 注意事项

需要设备/模拟器；app 测试断言 target package `com.wldmedical.capnoeasy`。

## 最小验证方式

`./gradlew :app:connectedDebugAndroidTest :hotmeltprint:connectedDebugAndroidTest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
