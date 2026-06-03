# SaveChartToPdfTask.createWaveformBitmap

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L339）。
领域：PDF 函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`SaveChartToPdfTask.createWaveformBitmap`
- ID / 别名：manual waveform bitmap, PDF 手绘波形网格
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：用 Canvas 手绘 0-14 秒网格、90 mmHg 纵轴和 CO2 曲线

## 补充职责

生成纸质报告单风格的 CO2 波形 bitmap，包含密集网格、固定 0-14 秒横轴、纵轴单位和黑色波形线；短段波形只绘制到对应秒数位置，横坐标范围仍保持一致。

## 关键 ID / 别名

- 定位别名：manual waveform bitmap, PDF 手绘波形网格
- 关键字段 / 方法：`Bitmap.createBitmap(1600, 260, ...)`、`REPORT_SEGMENT_POINTS`、`reportYAxisMax`、`Path`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`Bitmap.createBitmap(1600, 260, ...)`、`REPORT_SEGMENT_POINTS`、`reportYAxisMax`、`Path`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`

## 主要调用点

`SaveChartToPdfTask.addWaveformChart`。

## 注意事项

`mmHg` 报告纵轴固定显示到 90；非 `mmHg` 单位沿用当前 CO2 量程。所有 PDF 波形段横轴固定为 0-14 秒；PDF 波形不再依赖 MPAndroidChart 默认样式。

## 最小验证方式

`./gradlew :app:compileDebugKotlin`; 手动导出 PDF 检查网格、坐标和曲线。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
