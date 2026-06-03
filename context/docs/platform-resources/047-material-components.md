# Material Components

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L452）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/047-material-components.md`。

## 实体定位

- 实体：Material Components
- ID / 别名：`com.google.android.material:material:1.12.0`
- 源文件：`gradle/libs.versions.toml`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：hotmeltprint 模块声明的 Material Components 依赖

## 补充职责

Android View Material Components 依赖，当前由 hotmeltprint 声明。

## 关键 ID / 别名

`com.google.android.material:material:1.12.0`

## 关键字段 / 方法

`com.google.android.material:material:1.12.0`。

## 主要调用点

hotmeltprint 源码未检出直接 Material Components 调用。

## 注意事项

可能是历史/兼容依赖；移除前需验证库模块编译和打印 UI/上下游调用。

## 最小验证方式

`./gradlew :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
