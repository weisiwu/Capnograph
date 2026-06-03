# EtCo2LineChart

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L186）。
领域：组件。

## 实体定位

- 实体：EtCo2LineChart
- ID / 别名：CO2 chart, 波形图, 折线图
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：MPAndroidChart 的 Compose AndroidView 包装组件

## 补充职责

实时波形组件，监听 BlueToothKit.dataFlow 更新 MPAndroidChart；记录中监听 totalCO2WavedDataFlow，满 chunk 后压缩写入 CO2Data。

## 关键 ID / 别名

CO2 chart, 波形图, 折线图

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`

## 主要调用点

由页面 Content 或 BaseLayout 组合调用，回调向上交给 Activity/ViewModel。

## 注意事项

MPAndroidChart 的 Compose AndroidView 包装组件。

## 最小验证方式

./gradlew :app:assembleDebug；人工检查组件所在页面。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
