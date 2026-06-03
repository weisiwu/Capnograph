# PrintSetting

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L238）。
领域：打印。
聚合章节：Kit 与服务。

## 实体定位

- 实体：PrintSetting
- ID / 别名：print settings, 打印设置
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：共享打印设置对象；PDF 报告基础信息读取 `pdfDepart`、床位号、住院号和患者信息

## 补充职责

PDF/小票共享打印配置；PDF 报告基础信息区会读取 `pdfDepart`、`pdfBedNumber`、`pdfIDNumber`、`name`、`gender`、`age`。

## 关键 ID / 别名

- 定位别名：print settings, 打印设置
- 关键字段 / 方法：`hospitalName`、`reportName`、`isPDF`、`pdfDepart`、`pdfBedNumber`、`pdfIDNumber`、`name`、`gender`、`age`、`showTrendingChart`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`hospitalName`、`reportName`、`isPDF`、`pdfDepart`、`pdfBedNumber`、`pdfIDNumber`、`name`、`gender`、`age`、`showTrendingChart`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`LocalStorageKit.loadPrintSettingFromPreferences`、PDF/打印输出。

## 注意事项

全局 object，可被偏好读取覆盖。PDF 报告科室字段来自 `pdfDepart`，缺值时报告显示 `未填写`。

## 最小验证方式

`rg "PrintSetting\\."`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
