# Jetpack Compose BOM

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L442）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/037-jetpack-compose-bom.md`。

## 实体定位

- 实体：Jetpack Compose BOM
- ID / 别名：`androidx.compose:compose-bom:2024.04.01`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Compose UI、graphics、tooling、Material3、测试依赖版本平台

## 补充职责

Compose 版本平台，统一 UI、graphics、tooling、Material3 和测试依赖版本。

## 关键 ID / 别名

`androidx.compose:compose-bom:2024.04.01`

## 关键字段 / 方法

`androidx.compose:compose-bom:2024.04.01`；通过 `platform(libs.androidx.compose.bom)` 引入。

## 主要调用点

app 的 Compose runtime/UI/Material/test 依赖均受 BOM 约束。

## 注意事项

新增 Compose 库优先接入 BOM；不要给同一 BOM 管理坐标硬编码冲突版本。

## 最小验证方式

`./gradlew :app:dependencies --configuration debugRuntimeClasspath`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
