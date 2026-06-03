# ConstraintLayout

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L446）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/041-constraintlayout.md`。

## 实体定位

- 实体：ConstraintLayout
- ID / 别名：`androidx.constraintlayout:constraintlayout-core:1.1.0`, `constraintlayout:2.2.0`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的 ConstraintLayout 依赖

## 补充职责

ConstraintLayout core 和 View 依赖，当前由 app 声明。

## 关键 ID / 别名

`androidx.constraintlayout:constraintlayout-core:1.1.0`, `constraintlayout:2.2.0`

## 关键字段 / 方法

`androidx.constraintlayout:constraintlayout-core:1.1.0`、`androidx.constraintlayout:constraintlayout:2.2.0`。

## 主要调用点

源码未检出直接 `Constraint*` 调用；可能是历史/兼容声明。

## 注意事项

移除前先全局 `rg "Constraint" app/src/main/java app/src/main/res`。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
