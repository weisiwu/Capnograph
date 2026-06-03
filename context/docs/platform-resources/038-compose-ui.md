# Compose UI

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L443）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/038-compose-ui.md`。

## 实体定位

- 实体：Compose UI
- ID / 别名：`androidx.compose.ui:ui`, `ui-graphics`, `ui-tooling`, `ui-tooling-preview`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app Compose 基础 UI 与预览/调试工具

## 补充职责

Compose UI 基础、graphics、tooling 和 preview 依赖。

## 关键 ID / 别名

`androidx.compose.ui:ui`, `ui-graphics`, `ui-tooling`, `ui-tooling-preview`

## 关键字段 / 方法

`androidx.compose.ui:ui`、`ui-graphics`、`ui-tooling`、`ui-tooling-preview`。

## 主要调用点

`components`、`pages`、`ui.theme` 中的 Compose UI 构建和预览。

## 注意事项

`ui-tooling` 仅 debugImplementation，preview/tooling-preview 是 implementation。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
