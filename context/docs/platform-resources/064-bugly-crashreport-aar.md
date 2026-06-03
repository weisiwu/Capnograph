# Bugly CrashReport AAR

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L469）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/064-bugly-crashreport-aar.md`。

## 实体定位

- 实体：Bugly CrashReport AAR
- ID / 别名：`crashreport-4.1.9.3.aar`, `com.tencent.bugly.crashreport.CrashReport`
- 源文件：`app/libs/crashreport-4.1.9.3.aar`, `app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：本地 AAR 崩溃上报

## 补充职责

本地 Bugly 崩溃上报 AAR 依赖。

## 关键 ID / 别名

`crashreport-4.1.9.3.aar`, `com.tencent.bugly.crashreport.CrashReport`

## 关键字段 / 方法

`app/libs/crashreport-4.1.9.3.aar`，约 680K；类 `com.tencent.bugly.crashreport.CrashReport`。

## 主要调用点

`CapnoEasyApplication.kt` import CrashReport；Manifest 配置 `BUGLY_APPID=06c39f5912`、`BUGLY_APP_VERSION=v0.1.1.20250327.2101`、debug true。

## 注意事项

`CrashReport.initCrashReport(getApplicationContext())` 当前被注释；重新启用需网络权限和崩溃上报验证。

## 最小验证方式

`./gradlew :app:assembleDebug`；启用后真机制造测试崩溃

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
