# Jetpack Compose UI

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L62）。
领域：UI 平台。
实体级上下文：`context/docs/platform-resources/029-jetpack-compose-ui.md`。

## 实体定位

- 实体：Jetpack Compose UI
- ID / 别名：`buildFeatures.compose=true`, `androidx.compose`, `composeBom=2024.04.01`, Material3
- 源文件：`app/build.gradle.kts`, `gradle/libs.versions.toml`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块的 Compose、BOM、Material3、Navigation Compose 基础 UI 能力

## 补充职责

app 主要 UI 技术栈，支撑页面和组件中的 Compose 声明式 UI。

## 关键 ID / 别名

`buildFeatures.compose=true`, `androidx.compose`, `composeBom=2024.04.01`, Material3

## 关键字段 / 方法

`buildFeatures.compose=true`；Compose BOM `2024.04.01`；声明 UI、graphics、tooling-preview、Material3、Navigation Compose、Activity Compose。

## 主要调用点

`SplashActivity` 和继承 `BaseActivity` 的页面通过 `setContent` 渲染 Compose；组件在 `components` 包复用。

## 注意事项

页面导航当前主要使用 Intent，Navigation Compose 已声明但未检出 NavHost 使用。

## 最小验证方式

`./gradlew :app:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
