# Compose Foundation Android

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L451）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/046-compose-foundation-android.md`。

## 实体定位

- 实体：Compose Foundation Android
- ID / 别名：`foundation-layout-android:1.7.2`, `foundation-android:1.7.8`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 和 hotmeltprint 的 Compose foundation 相关依赖

## 补充职责

Compose foundation/foundation-layout 依赖。

## 关键 ID / 别名

`foundation-layout-android:1.7.2`, `foundation-android:1.7.8`

## 关键字段 / 方法

`foundation-layout-android:1.7.2`、`foundation-android:1.7.8`。

## 主要调用点

app 声明 `foundation-layout-android`；hotmeltprint 声明 `foundation-android`。

## 注意事项

`app/build.gradle.kts` 中 `foundation-layout-android` 重复声明 4 次。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
