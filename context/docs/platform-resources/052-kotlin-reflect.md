# Kotlin Reflect

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L457）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/052-kotlin-reflect.md`。

## 实体定位

- 实体：Kotlin Reflect
- ID / 别名：`org.jetbrains.kotlin:kotlin-reflect`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：`getValueByKey` 使用 Kotlin 反射读取属性

## 补充职责

Kotlin 反射库，支撑常量对象按 key 取值。

## 关键 ID / 别名

`org.jetbrains.kotlin:kotlin-reflect`

## 关键字段 / 方法

`org.jetbrains.kotlin:kotlin-reflect`。

## 主要调用点

`CapnoEasyConstant.getValueByKey` 使用反射读取对象属性。

## 注意事项

反射 key/value 改动需要验证常量映射和本地化资源读取。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
