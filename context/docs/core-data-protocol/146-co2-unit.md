# CO2_UNIT

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L417）。
领域：常量。
聚合章节：设置与常量。

## 实体定位

- 实体：CO2_UNIT
- ID / 别名：`MMHG`, `KPA`, `PERCENT`, CO2单位
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：取值：`mmHg`、`kPa`、`%`

## 补充职责

CO2 显示单位；PDF 报告的波形指标、纵轴单位和 EtCO2 参考值都跟随该单位。

## 关键 ID / 别名

- 定位别名：`MMHG`, `KPA`, `PERCENT`, CO2单位
- 值 / 字段：`MMHG("mmHg")`, `KPA("kPa")`, `PERCENT("%")`

## 关键字段 / 方法

- 主要字段、方法或协议值：`MMHG("mmHg")`, `KPA("kPa")`, `PERCENT("%")`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

显示设置、PDF 单位、设备 Settings/SetCO2Unit。

## 注意事项

下发映射：MMHG=0、KPA=1、PERCENT=2。PDF EtCO2 参考范围以 `32-42mmHg` 为基准配置，`kPa` 导出时按 `1mmHg = 0.133322kPa` 换算，`%` 导出时按 `mmHg / 760 * 100` 换算；单位未知时不显示参考行。

## 最小验证方式

检查对应源码入口。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
