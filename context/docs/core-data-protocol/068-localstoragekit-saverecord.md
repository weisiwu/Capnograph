# LocalStorageKit.saveRecord

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L322）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.saveRecord`
- ID / 别名：insert record, 保存记录
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 备注：创建 Record、设置 `currentRecordId`、可预生成 PDF，并插入 Room

## 补充职责

创建 Record，设置当前记录 ID，插入 Room，可尝试预生成 PDF。

## 关键 ID / 别名

- 定位别名：insert record, 保存记录
- 关键字段 / 方法：`currentRecordId`、`generateDateIndex`、`generatePatientIndex`、`pdfFilePath`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`currentRecordId`、`generateDateIndex`、`generatePatientIndex`、`pdfFilePath`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

主页开始记录。

## 注意事项

预生成 PDF 当前传 `data = listOf()`，主导出在详情页重新生成。

## 最小验证方式

检查 TODO 段

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
