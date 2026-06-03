# Android application plugin

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L53）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/020-android-application-plugin.md`。

## 实体定位

- 实体：Android application plugin
- ID / 别名：`libs.plugins.android.application`, `com.android.application`, `:app`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：主应用模块使用的 Android Application 插件

## 补充职责

让 `:app` 作为可安装 Android 应用构建，生成 APK、Manifest 合并和资源处理任务。

## 关键 ID / 别名

`libs.plugins.android.application`, `com.android.application`, `:app`

## 关键字段 / 方法

`alias(libs.plugins.android.application)` 应用在 `app/build.gradle.kts`；applicationId 为 `com.wldmedical.capnoeasy`。

## 主要调用点

控制 app 的 defaultConfig、buildTypes、packaging、resources 和 APK 输出。

## 注意事项

主应用 Manifest 的 launcher Activity 和 Application 只在 application 模块生效。

## 最小验证方式

`./gradlew :app:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
