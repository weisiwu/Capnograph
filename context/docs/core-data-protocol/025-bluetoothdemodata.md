# BluetoothDemoData

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L235）。
领域：蓝牙。
聚合章节：Kit 与服务。

## 实体定位

- 实体：BluetoothDemoData
- ID / 别名：demo data, 演示数据
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BluetoothDemoData.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：随机/演示波形数据生成器

## 补充职责

生成正弦 demo 波形点。

## 关键 ID / 别名

- 定位别名：demo data, 演示数据
- 关键字段 / 方法：`POINTS_PER_SECOND = 100`、`MAX_POINTS = 500`、`RandomCurveGenerator`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`POINTS_PER_SECOND = 100`、`MAX_POINTS = 500`、`RandomCurveGenerator`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BluetoothDemoData.kt`

## 主要调用点

当前主要用于本地 demo/测试，不在主业务链路中调用。

## 注意事项

`main()` 会 sleep 很久，不作为 app 入口。

## 最小验证方式

`rg "RandomCurveGenerator"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
