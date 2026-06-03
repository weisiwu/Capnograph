# Hilt Navigation Compose

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L456）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/051-hilt-navigation-compose.md`。

## 实体定位

- 实体：Hilt Navigation Compose
- ID / 别名：`androidx.hilt:hilt-navigation-compose:1.1.0-alpha01`
- 源文件：`app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的 Hilt Compose 辅助库

## 补充职责

Hilt 与 Navigation Compose 的 ViewModel 辅助库。

## 关键 ID / 别名

`androidx.hilt:hilt-navigation-compose:1.1.0-alpha01`

## 关键字段 / 方法

`androidx.hilt:hilt-navigation-compose:1.1.0-alpha01`。

## 主要调用点

app 声明依赖；源码未检出 `hiltViewModel` 直接调用。

## 注意事项

若引入 Navigation Compose 作用域 ViewModel，需要补充页面路由和 ViewModel 生命周期上下文。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
