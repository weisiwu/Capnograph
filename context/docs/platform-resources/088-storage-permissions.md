# Storage permissions

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L503）。
领域：清单。
实体级上下文：`context/docs/platform-resources/088-storage-permissions.md`。

## 实体定位

- 实体：Storage permissions
- ID / 别名：MANAGE_EXTERNAL_STORAGE, READ/WRITE_EXTERNAL_STORAGE, 存储权限
- 源文件：`app/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：存储、导出和备份权限

## 补充职责

存储、导出、备份和 PDF 保存相关权限声明。

## 关键 ID / 别名

MANAGE_EXTERNAL_STORAGE, READ/WRITE_EXTERNAL_STORAGE, 存储权限

## 关键字段 / 方法

`MANAGE_EXTERNAL_STORAGE`、`READ_EXTERNAL_STORAGE`、`WRITE_EXTERNAL_STORAGE maxSdkVersion=28`；Application `requestLegacyExternalStorage=true`。

## 主要调用点

`HistoryRecordDetailActivity`、`PDFKit`、DatabaseBackupHelper/文件导出相关流程。

## 注意事项

Android 11+ `MANAGE_EXTERNAL_STORAGE` 审核敏感；优先验证 SAF 导出路径。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`; Android 11+ 导出/备份手动验证

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
