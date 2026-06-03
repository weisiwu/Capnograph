# DatabaseBackupHelperManager

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L234）。
领域：存储。
聚合章节：Kit 与服务。

## 实体定位

- 实体：DatabaseBackupHelperManager
- ID / 别名：backup manager, 备份管理单例
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：备份辅助访问 Manager 对象

## 补充职责

备份 helper 单例入口。

## 关键 ID / 别名

- 定位别名：backup manager, 备份管理单例
- 关键字段 / 方法：`dbBackupHelperKit`、`initialize(application)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`dbBackupHelperKit`、`initialize(application)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`

## 主要调用点

`CapnoEasyApplication.onCreate`。

## 注意事项

上下文固定为 Application。

## 最小验证方式

`rg "DatabaseBackupHelperManager"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
