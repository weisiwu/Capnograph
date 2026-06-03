# kits` package

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L38）。
领域：模块。
实体级上下文：`context/docs/platform-resources/010-kits-package.md`。

## 实体定位

- 实体：`kits` package
- ID / 别名：service kits, 工具/服务层
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：蓝牙、协议、存储、PDF、音频、图片和备份能力

## 补充职责

工具/服务层，集中 BLE、协议、Room/SharedPreferences、PDF、报警音、图片选择、数据库备份和打印协议能力。

## 关键 ID / 别名

service kits, 工具/服务层

## 关键字段 / 方法

关键实体/文件：AlertAudioKit, BlueToothKit, BlueToothTaskQueueKit, BluetoothDemoData, CapnoEasyProtocalKit, DatabaseBackupHelperKit, ImageSelectorKit, LocalStorageKit, PDFKit, PrintProtocalKit。

## 主要调用点

涉及蓝牙、存储、PDF、音频和备份的变更通常需要真机或文件系统回归，不应只看 UI 编译。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
