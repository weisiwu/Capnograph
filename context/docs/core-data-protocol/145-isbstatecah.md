# ISBStateCAH

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L411）。
领域：协议。
聚合章节：BLE 协议 ID。

## 实体定位

- 实体：ISBStateCAH
- ID / 别名：CAH software info fields, CAH 软件信息字段
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：软件版本、生产日期和模块名称

## 补充职责

CAH 软件信息伪 ISB 区分字段。

## 关键 ID / 别名

- 定位别名：CAH software info fields, CAH 软件信息字段
- 协议 ID / 值：`99`, `98`, `97`

## 关键字段 / 方法

- 主要字段、方法或协议值：`99`, `98`, `97`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt`

## 主要调用点

文档语义；解析实际按字符串正则。

## 注意事项

代码未直接分派这些 enum 值。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
