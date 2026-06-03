# kits.dbmigration` package

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L39）。
领域：模块。
实体级上下文：`context/docs/platform-resources/011-kits-dbmigration-package.md`。

## 实体定位

- 实体：`kits.dbmigration` package
- ID / 别名：Room migrations, 数据库迁移
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/`
- 原始补充上下文：`app/data_version_list.txt`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：Room 版本迁移脚本目录

## 补充职责

Room 数据库迁移脚本目录，目前包含 `DatabaserMigration_FROM1_TO2.kt`。

## 关键 ID / 别名

Room migrations, 数据库迁移

## 关键字段 / 方法

关键实体/文件：AlertAudioKit, BlueToothKit, BlueToothTaskQueueKit, BluetoothDemoData, CapnoEasyProtocalKit, DatabaseBackupHelperKit, ImageSelectorKit, LocalStorageKit, PDFKit, PrintProtocalKit。

## 主要调用点

数据库事实以 `LocalStorageKit.kt`、`app/data_version_list.txt` 和 `app/schemas/...` 为准；迁移需兼容历史 JSON/表结构。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
