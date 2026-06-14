<!-- context-seed:start -->
# DatabaseBackupHelper

## 定位

- ID: `AND-DBH`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `DatabaseBackupHelper` 是 Android 端的 Room 数据库备份工具。
- 在应用启动后将数据库文件从内部存储备份到外部存储（Download 目录）。
- 支持数据恢复。
- 通过 `DatabaseBackupHelperManager` 单例管理初始化。
- 在 `CapnoEasyApplication.onCreate()` 中启动。
<!-- context-seed:end -->
