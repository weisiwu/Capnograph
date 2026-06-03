# Maven repositories

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L51）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/018-maven-repositories.md`。

## 实体定位

- 实体：Maven repositories
- ID / 别名：`google`, `mavenCentral`, `gradlePluginPortal`, `jitpack.io`, Huawei repo, Aliyun mirrors
- 源文件：`settings.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：插件和依赖仓库配置

## 补充职责

插件和依赖解析仓库配置。

## 关键 ID / 别名

`google`, `mavenCentral`, `gradlePluginPortal`, `jitpack.io`, Huawei repo, Aliyun mirrors

## 关键字段 / 方法

pluginManagement: `google`、`mavenCentral`、`gradlePluginPortal`；dependencyResolution: `google`、`mavenCentral`、`jitpack.io`、Huawei、Aliyun mirrors。

## 主要调用点

Gradle 解析 AGP、Kotlin、AndroidX、JitPack 依赖和本地镜像依赖时使用。

## 注意事项

`RepositoriesMode.FAIL_ON_PROJECT_REPOS` 意味着新增模块仓库要改 settings，不应写到模块 build 文件。

## 最小验证方式

`./gradlew :app:dependencies --configuration debugRuntimeClasspath`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
