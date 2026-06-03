# asphyxiationTimeRange

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L430）。
领域：常量。
聚合章节：设置与常量。

## 实体定位

- 实体：asphyxiationTimeRange
- ID / 别名：窒息时间范围
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：当前范围：10 到 60

## 补充职责

窒息时间设置范围。

## 关键 ID / 别名

- 定位别名：窒息时间范围
- 值 / 字段：`10f..60f`

## 关键字段 / 方法

- 主要字段、方法或协议值：`10f..60f`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

模块设置。

## 注意事项

下发到 `ISBState84H.NoBreaths`。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
