# AndroidX Core SplashScreen

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L454）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/049-androidx-core-splashscreen.md`。

## 实体定位

- 实体：AndroidX Core SplashScreen
- ID / 别名：`androidx.core:core-splashscreen:1.0.1`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/pages/SplashActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的系统 SplashScreen 兼容库

## 补充职责

Android 12 SplashScreen 兼容库，当前由 app 声明。

## 关键 ID / 别名

`androidx.core:core-splashscreen:1.0.1`

## 关键字段 / 方法

`androidx.core:core-splashscreen:1.0.1`。

## 主要调用点

`SplashActivity` 当前自绘 Compose 启动动画，源码未检出 `installSplashScreen`。

## 注意事项

如果真正启用系统 Splash API，需要同步启动页上下文和主题行为。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
