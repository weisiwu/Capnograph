# DatabaseBackupHelperManager.initialize

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L348）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`DatabaseBackupHelperManager.initialize`
- ID / 别名：backup singleton init, 备份单例初始化
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：创建 DatabaseBackupHelper 单例

## 补充职责

创建备份 helper 单例。

## 关键 ID / 别名

- 定位别名：backup singleton init, 备份单例初始化
- 关键字段 / 方法：`DatabaseBackupHelper(application)`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`DatabaseBackupHelper(application)`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`

## 主要调用点

`CapnoEasyApplication.onCreate`。

## 注意事项

已初始化时不替换。

## 最小验证方式

`rg "dbBackupHelperKit"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
