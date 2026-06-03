# AppStateModel.updateTotalCO2WavedData

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L155）。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updateTotalCO2WavedData`
- ID / 别名：waveform memory buffer, 记录波形缓存
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：记录中追加带采样时间的 CO2WavePointData；停止后清空内存数据和 StateFlow

## 补充职责

记录中追加带采样时间的 CO2WavePointData；停止后清空内存数据和 StateFlow。

## 关键 ID / 别名

waveform memory buffer, 记录波形缓存

## 关键字段 / 方法

- 主要实体或方法：`AppStateModel.updateTotalCO2WavedData`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

由页面、组件、蓝牙回调或本地存储回读调用，用于写入 AppState。

## 注意事项

记录中追加 CO2WavePointData 时由蓝牙数据入口写入 `sampleTimeMillis = System.currentTimeMillis()`，供 PDF 报告按真实采样时间切段和定位横坐标；停止后清空内存数据和 StateFlow。

## 最小验证方式

./gradlew :app:assembleDebug；rg 函数名确认调用点和状态副作用。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
