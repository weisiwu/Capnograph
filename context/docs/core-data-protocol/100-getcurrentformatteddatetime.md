# getCurrentFormattedDateTime

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L354）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`getCurrentFormattedDateTime`
- ID / 别名：print timestamp, 打印时间格式化
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：小票打印当前时间，格式为 `yyyy-MM-dd HH:mm:ss`

## 补充职责

生成小票打印时间。

## 关键 ID / 别名

- 定位别名：print timestamp, 打印时间格式化
- 关键字段 / 方法：格式 `yyyy-MM-dd HH:mm:ss`。

## 关键字段 / 方法

- 主要字段、方法或协议值：格式 `yyyy-MM-dd HH:mm:ss`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`HotmeltPinter.print`。

## 注意事项

使用设备本地时间。

## 最小验证方式

检查 formatter

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
