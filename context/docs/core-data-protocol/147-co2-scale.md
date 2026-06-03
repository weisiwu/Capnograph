# CO2_SCALE

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L418）。
领域：常量。
聚合章节：设置与常量。

## 实体定位

- 实体：CO2_SCALE
- ID / 别名：waveform scale, CO2量程
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：mmHg/kPa/百分比显示量程

## 补充职责

波形 Y 轴量程。

## 关键 ID / 别名

- 定位别名：waveform scale, CO2量程
- 值 / 字段：`50/60/75`, `6.7/8/10`, `6.6/7.9/9.9`

## 关键字段 / 方法

- 主要字段、方法或协议值：`50/60/75`, `6.7/8/10`, `6.6/7.9/9.9`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

图表、PDF、打印、设备 F2H CO2Scale。

## 注意事项

下发只按 small/middle/large 映射 1/0/2。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
