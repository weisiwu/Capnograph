# LocalStorageKit.savePatient

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L321）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.savePatient`
- ID / 别名：insert patient, 保存患者
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：IO 线程插入 Patient 并更新内存列表

## 补充职责

IO 线程插入患者并更新内存列表。

## 关键 ID / 别名

- 定位别名：insert patient, 保存患者
- 关键字段 / 方法：`patientDto().insertPatient`、`patients.add`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`patientDto().insertPatient`、`patients.add`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`MainActivity.onNavBarRightClick` 开始记录。

## 注意事项

插入后 Patient 的 auto id 不回填到传入对象。

## 最小验证方式

检查 DAO

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
