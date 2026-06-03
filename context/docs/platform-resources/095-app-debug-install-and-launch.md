# app debug install and launch task

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（构建与测试入口）。
领域：构建。
实体级上下文：`context/docs/platform-resources/095-app-debug-install-and-launch.md`。

## 实体定位

- 实体：app debug install and launch task
- ID / 别名：`:app:installDebugAndLaunch`, `:app:reinstallDebugAndLaunch`, `:app:launchDebugApp`, `:app:uninstallDebugApp`, `adbSerial`
- 源文件：`app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`
- 原始补充上下文：`context/docs/platform-resources/002-app-module.md`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：Debug 安装后自动启动；签名冲突时可先卸载再安装

## 补充职责

为连接的 Android 设备提供 Debug 包安装后自动打开应用的本地开发入口。

## 关键 ID / 别名

`:app:installDebugAndLaunch`, `:app:reinstallDebugAndLaunch`, `:app:launchDebugApp`, `:app:uninstallDebugApp`, `adbSerial`

## 关键字段 / 方法

`debugApplicationId` 为 `com.wldmedical.capnoeasy`。启动命令通过 `adb shell monkey -p com.wldmedical.capnoeasy -c android.intent.category.LAUNCHER 1` 触发 Manifest 中的 launcher Activity。卸载命令先执行 `adb uninstall com.wldmedical.capnoeasy`，失败时兜底执行 `adb shell pm uninstall --user 0 com.wldmedical.capnoeasy`。传入 `-PadbSerial=<device-serial>` 时，自定义 ADB 启动/卸载命令会追加 `adb -s <device-serial>`。

## 主要调用点

`./gradlew :app:installDebugAndLaunch` 会先执行 Android Gradle Plugin 的 `installDebug`，成功后启动应用。`./gradlew :app:reinstallDebugAndLaunch` 会先执行 `uninstallDebugApp`，再安装并启动，用于设备上已有同包名但签名不一致的版本。

## 注意事项

Android 不能忽略签名校验覆盖安装；签名不一致时只能先卸载同包名应用再安装 Debug 包。`reinstallDebugAndLaunch` 会删除设备上该应用的本地数据。多设备连接时，启动/卸载命令可用 `-PadbSerial=<device-serial>` 指定设备。

## 最小验证方式

`./gradlew :app:tasks --all` 确认任务存在；连接设备后执行 `./gradlew :app:installDebugAndLaunch` 或 `./gradlew :app:reinstallDebugAndLaunch`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
