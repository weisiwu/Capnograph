# Room Compiler

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L463）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/058-room-compiler.md`。

## 实体定位

- 实体：Room Compiler
- ID / 别名：`androidx.room:room-compiler:2.5.2`, kapt
- 源文件：`app/build.gradle.kts`
- 原始补充上下文：`app/data_version_list.txt`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Room 注解处理器

## 补充职责

Room 注解处理器，通过 kapt 生成数据库代码。

## 关键 ID / 别名

`androidx.room:room-compiler:2.5.2`, kapt

## 关键字段 / 方法

`androidx.room:room-compiler:2.5.2`；`kapt(...)`。

## 主要调用点

处理 `@Database`、实体和 DAO 注解；schema 输出到 `app/schemas`。

## 注意事项

Room 编译错误多出现在 kapt 阶段；只改文档不需要跑 kapt。

## 最小验证方式

`./gradlew :app:kaptDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
