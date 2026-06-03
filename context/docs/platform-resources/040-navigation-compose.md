# Navigation Compose

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L445）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/040-navigation-compose.md`。

## 实体定位

- 实体：Navigation Compose
- ID / 别名：`androidx.navigation:navigation-compose:2.8.5`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：已声明依赖；当前页面导航主要使用 Intent

## 补充职责

Compose 导航库，当前作为已声明依赖。

## 关键 ID / 别名

`androidx.navigation:navigation-compose:2.8.5`

## 关键字段 / 方法

`androidx.navigation:navigation-compose:2.8.5`。

## 主要调用点

源码未检出 `NavHost`/`rememberNavController`；页面导航主要使用 Intent。

## 注意事项

如果改用 NavHost，需要同步 app-ui-workflows 的页面路由上下文。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
