# AndroidX AppCompat

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L448）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/043-androidx-appcompat.md`。

## 实体定位

- 实体：AndroidX AppCompat
- ID / 别名：`androidx.appcompat:appcompat:1.7.0`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：语言切换使用 AppCompatDelegate；两个模块均声明

## 补充职责

AppCompat 兼容库，明确用于应用内语言切换。

## 关键 ID / 别名

`androidx.appcompat:appcompat:1.7.0`

## 关键字段 / 方法

`androidx.appcompat:appcompat:1.7.0`；两个模块声明。

## 主要调用点

`AppStateModel.updateLanguage` 调用 `AppCompatDelegate.setApplicationLocales`。

## 注意事项

语言切换需结合 `locales_config` 和 values-en/values 资源真机验证。

## 最小验证方式

`./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
