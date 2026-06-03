# Gradle settings

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L27）。
领域：构建。
实体级上下文：`context/docs/platform-resources/004-gradle-settings.md`。

## 实体定位

- 实体：Gradle settings
- ID / 别名：repositories, included modules, Gradle 设置
- 源文件：`settings.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：根工程模块拓扑和仓库配置

## 补充职责

根工程 settings 文件，统一 pluginManagement、dependencyResolutionManagement、仓库来源和模块包含关系。

## 关键 ID / 别名

repositories, included modules, Gradle 设置

## 关键字段 / 方法

插件仓库为 `google`、`mavenCentral`、`gradlePluginPortal`；依赖仓库为 Google、Maven Central、JitPack、Huawei repo、Aliyun google/central/jcenter mirror。

## 主要调用点

Gradle 配置阶段读取该文件；`RepositoriesMode.FAIL_ON_PROJECT_REPOS` 禁止模块内另行声明项目仓库。

## 注意事项

三方依赖里含 JitPack 坐标，移除 `https://jitpack.io` 会影响 wheel picker、MPAndroidChart、AndroidPdfViewer 等解析。

## 最小验证方式

`./gradlew projects` 或 `./gradlew help`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
