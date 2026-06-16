# Crashreport AAR

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L492）。
领域：依赖。
实体级上下文：`context/docs/platform-resources/082-crashreport-aar.md`。

## 实体定位

- 实体：Crashreport AAR
- ID / 别名：Bugly crashreport
- 源文件：`app/libs/crashreport-4.1.9.3.aar`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：内置崩溃上报 AAR

## 补充职责

内置 Bugly CrashReport AAR 二进制资源。

## 关键 ID / 别名

Bugly crashreport

## 关键字段 / 方法

`app/libs/crashreport-4.1.9.3.aar`，约 680K。

## 主要调用点

通过 app `fileTree(include=*.aar, dir=libs)` 引入；`ErrorReporter` 封装 CrashReport 初始化、上下文附加和已捕获异常上报。

## 注意事项

与三方依赖实体 `Bugly CrashReport AAR` 指向同一二进制；Application 启动时通过 `ErrorReporter.initialize` 启用，release 构建关闭 debug。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
