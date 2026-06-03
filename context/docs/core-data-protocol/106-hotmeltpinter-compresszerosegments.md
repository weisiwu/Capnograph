# HotmeltPinter.compressZeroSegments

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L360）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`HotmeltPinter.compressZeroSegments`
- ID / 别名：compress zero waveform, 压缩零值波形段
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：打印前将连续零值段压缩到默认 5%

## 补充职责

压缩连续 0 值波形段。

## 关键 ID / 别名

- 定位别名：compress zero waveform, 压缩零值波形段
- 关键字段 / 方法：`compressRatio = 0.05f`、中间零段至少保留 1 点。

## 关键字段 / 方法

- 主要字段、方法或协议值：`compressRatio = 0.05f`、中间零段至少保留 1 点。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`washData`。

## 注意事项

只处理值精确等于 `0f` 的点。

## 最小验证方式

检查 Segment 逻辑

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
