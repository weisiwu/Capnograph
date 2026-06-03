# Compose Material3

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L444）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/039-compose-material3.md`。

## 实体定位

- 实体：Compose Material3
- ID / 别名：`androidx.compose.material3:material3`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：页面和组件使用的 Material3 控件

## 补充职责

Material3 Compose 控件和主题能力。

## 关键 ID / 别名

`androidx.compose.material3:material3`

## 关键字段 / 方法

`androidx.compose.material3:material3`，由 BOM 管理版本。

## 主要调用点

`CapnoEasyTheme` 使用 `MaterialTheme`；页面/组件使用 Material3 控件。

## 注意事项

Android 12+ dynamic color 可能覆盖静态色值。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
