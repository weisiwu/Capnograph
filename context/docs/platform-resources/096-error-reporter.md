# ErrorReporter

来源批次：错误上报能力补充。
定位入口：`context/entity-id-mapping.md`。
领域：错误上报。
实体级上下文：`context/docs/platform-resources/096-error-reporter.md`。

## 实体定位

- 实体：ErrorReporter
- ID / 别名：错误上报, 非致命异常上报, Bugly wrapper
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/ErrorReporter.kt`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：Bugly CrashReport 的项目内统一封装

## 补充职责

统一初始化 Bugly CrashReport，附加 app/page/activity/last error 上下文，并为已捕获异常和协程异常提供非致命上报入口。

## 关键 ID / 别名

错误上报, 非致命异常上报, Bugly wrapper

## 关键字段 / 方法

`initialize`、`report`、`coroutineExceptionHandler`、`setContext`、`setAppForeground`；按 stage 对非致命异常做 60 秒节流。

## 主要调用点

`CapnoEasyApplication.onCreate` 初始化；`BaseActivity.updatePageScene` 写入页面上下文；Main/History/PDF/BlueTooth/DatabaseBackup/ImageSelector/AlertAudio 等边界上报已捕获异常。

## 注意事项

metadata 只记录阶段、页面、数据量、文件存在性等诊断信息，避免直接上传患者姓名、住院号、床号或设备序列号。Bugly app id/version/debug 来自 Manifest 占位符与运行时策略；release 构建关闭 debug。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`；真机/模拟器冷启动并触发一次可控非致命异常后在 Bugly 后台验证。

## 同步要求

- 如果错误上报 SDK、初始化策略、metadata 范围或调用点变化，同步更新本文档、`context/entity-id-mapping.md`、Bugly/Manifest 相关平台文档。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
