# Android library plugin

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L54）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/021-android-library-plugin.md`。

## 实体定位

- 实体：Android library plugin
- ID / 别名：`libs.plugins.android.library`, `com.android.library`, `:hotmeltprint`
- 源文件：`gradle/libs.versions.toml`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：热敏打印库模块使用的 Android Library 插件

## 补充职责

让 `:hotmeltprint` 作为 Android Library 构建，可被 app 依赖和参与 Manifest/资源合并。

## 关键 ID / 别名

`libs.plugins.android.library`, `com.android.library`, `:hotmeltprint`

## 关键字段 / 方法

`alias(libs.plugins.android.library)` 应用在 `hotmeltprint/build.gradle.kts`；namespace 为 `com.wldmedical.hotmeltprint`。

## 主要调用点

输出 AAR 给 app 使用，同时参与 Java/Kotlin 编译和 consumer proguard 配置。

## 注意事项

库 Manifest 为空；权限和 Application 不在库内声明。

## 最小验证方式

`./gradlew :hotmeltprint:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
