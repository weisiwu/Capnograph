# EtCo2LineChart

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L282）。
领域：UI 函数。

## 实体定位

- 实体：`EtCo2LineChart`
- ID / 别名：realtime chart composable, 实时波形组件函数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：监听 `BlueToothKit.dataFlow` 绘制实时波形；记录中按 chunk 写入 Room

## 补充职责

监听 `BlueToothKit.dataFlow` 绘制实时波形；记录中按 chunk 写入 Room。

## 关键 ID / 别名

realtime chart composable, 实时波形组件函数

## 关键字段 / 方法

- 主要实体或方法：`EtCo2LineChart`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`

## 主要调用点

由 Activity.Content、BaseLayout.float 或其他组件组合调用；输入状态通常来自 AppStateModel。

## 注意事项

监听 `BlueToothKit.dataFlow` 绘制实时波形；记录中按 chunk 写入 Room。

## 最小验证方式

./gradlew :app:assembleDebug；人工检查对应页面渲染和回调。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
