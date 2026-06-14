<!-- context-seed:start -->
# CapnoEasyApplication

## 定位

- ID: `AND-CEA`
- 类型: `class` (Application)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt:10`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CapnoEasyApplication` 是 Android 端的 Application 入口类，使用 `@HiltAndroidApp` 注解。
- **核心功能**：
  - 初始化 Bugly 崩溃收集（当前被注释）。
  - 初始化 Room 数据库（`AppDatabase`）。
  - 初始化数据库备份（`DatabaseBackupHelper`）。
  - 通过 `registerActivityLifecycleCallbacks` 跟踪所有 Activity 的创建/销毁，提供 `getActivityCount()` 方法。
- `companion object` 提供 `context` 静态引用。

## 调用链

- 在 `AndroidManifest.xml` 中配置为默认 Application。
- `BaseActivity` 的 `onBackPressed()` 使用 `getActivityCount()` 判断退出二次确认。
- 与 iOS 端的 `CapnoGraphApp` 对应。
<!-- context-seed:end -->
