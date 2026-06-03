# AndroidX Activity Compose

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L441）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/036-androidx-activity-compose.md`。

## 实体定位

- 实体：AndroidX Activity Compose
- ID / 别名：`androidx.activity:activity-compose:1.9.3`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Activity 内 `setContent` 和 Compose 集成

## 补充职责

Activity 与 Compose 集成库，提供 Activity `setContent` 等能力。

## 关键 ID / 别名

`androidx.activity:activity-compose:1.9.3`

## 关键字段 / 方法

`androidx.activity:activity-compose:1.9.3`；alias `libs.androidx.activity.compose`。

## 主要调用点

SplashActivity 和 BaseActivity 派生页面通过 Compose 渲染内容。

## 注意事项

Activity/Compose 升级需回归页面启动和生命周期。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
