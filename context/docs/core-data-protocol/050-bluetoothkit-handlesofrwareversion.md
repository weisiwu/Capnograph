# BlueToothKit.handleSofrWareVersion

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L304）。
领域：协议函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.handleSofrWareVersion`
- ID / 别名：parse CAH software, 解析软件版本
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：解析软件版本和生产日期；函数名当前拼写为 `handleSofrWareVersion`

## 补充职责

解析 CAH 软件版本、生产日期。

## 关键 ID / 别名

- 定位别名：parse CAH software, 解析软件版本
- 关键字段 / 方法：正则 `(\\d{2})\\s(\\d{2}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2})`、`productionDate`。

## 关键字段 / 方法

- 主要字段、方法或协议值：正则 `(\\d{2})\\s(\\d{2}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2})`、`productionDate`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

`getSpecificValue`。

## 注意事项

会移除控制字符和尾部连字符。

## 最小验证方式

`rg "rawSoftWareVersion"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
