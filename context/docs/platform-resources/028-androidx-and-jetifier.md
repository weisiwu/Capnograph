# AndroidX and Jetifier

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L61）。
领域：Android 平台。
实体级上下文：`context/docs/platform-resources/028-androidx-and-jetifier.md`。

## 实体定位

- 实体：AndroidX and Jetifier
- ID / 别名：`android.useAndroidX=true`, `android.enableJetifier=true`, `android.nonTransitiveRClass=true`
- 源文件：`gradle.properties`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：AndroidX、Jetifier 和非传递 R 类配置

## 补充职责

全局 AndroidX、Jetifier、R class 和打包兼容开关。

## 关键 ID / 别名

`android.useAndroidX=true`, `android.enableJetifier=true`, `android.nonTransitiveRClass=true`

## 关键字段 / 方法

`android.useAndroidX=true`、`android.enableJetifier=true`、`android.nonTransitiveRClass=true`、`android.zipAlign=true`。AGP 8 已移除 `android.enableR8` 项目属性；release R8 压缩由 `app/build.gradle.kts` 的 `isMinifyEnabled=true` 控制。

## 主要调用点

Gradle Android 插件处理依赖迁移、R 生成和 release 优化时读取。

## 注意事项

Jetifier 对旧 support 依赖兼容有影响；AndroidPdfViewer 显式排除 `com.android.support`。

## 最小验证方式

`./gradlew :app:assembleDebug`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
