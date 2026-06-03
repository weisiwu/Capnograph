# Coil

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L458）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/053-coil.md`。

## 实体定位

- 实体：Coil
- ID / 别名：`io.coil-kt:coil:2.4.0`, `coil-compose:2.4.0`
- 源文件：`app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的图片加载依赖

## 补充职责

图片加载依赖，包含基础 Coil 和 Compose 扩展。

## 关键 ID / 别名

`io.coil-kt:coil:2.4.0`, `coil-compose:2.4.0`

## 关键字段 / 方法

`io.coil-kt:coil:2.4.0`、`io.coil-kt:coil-compose:2.4.0`。

## 主要调用点

app 声明依赖；源码未检出 `coil`/`AsyncImage` 直接调用。

## 注意事项

当前更像预留依赖；启用图片加载后需补调用点和缓存/网络验证。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
