# HotmeltPinter.startProcessingData

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L359）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`HotmeltPinter.startProcessingData`
- ID / 别名：process print waveform, 打印波形处理
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：清洗波形、生成 Bitmap、旋转 90 度后写入打印机

## 补充职责

清洗波形、生成 Bitmap、旋转 90 度并写入打印机。

## 关键 ID / 别名

- 定位别名：process print waveform, 打印波形处理
- 关键字段 / 方法：`washData`、`generateWaveformBitmapNew`、`Matrix.postRotate(90f)`、`esc.drawImage`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`washData`、`generateWaveformBitmapNew`、`Matrix.postRotate(90f)`、`esc.drawImage`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

`print`。

## 注意事项

使用 `GlobalScope.launch`，主线程生成 chart。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
