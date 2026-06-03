# AppStateModel.updatePdfEventContextSeconds

来源批次：PDF 长记录报告策略补充。
定位入口：`context/entity-id-mapping.md`。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updatePdfEventContextSeconds`
- ID / 别名：PDF event context seconds, PDF异常上下文秒数
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：更新并保存 PDF 异常上下文秒数状态；当前 PDF 导出固定按 15 秒连续波形切段，不再使用该值

## 补充职责

更新 AppState 中的 PDF 异常片段上下文秒数。默认值来自 `PrintSetting.DEFAULT_PDF_EVENT_CONTEXT_SECONDS`，当前仍用于设置页状态和偏好保存，但 PDF 报告已改为固定 15 秒连续波形段，不再读取该值。

## 关键 ID / 别名

- 定位别名：PDF event context seconds, PDF异常上下文秒数
- 关键字段 / 方法：`pdfEventContextSeconds`、`updatePdfEventContextSeconds`、`DEFAULT_PDF_EVENT_CONTEXT_SECONDS`、`MIN_PDF_EVENT_CONTEXT_SECONDS`、`MAX_PDF_EVENT_CONTEXT_SECONDS`

## 关键字段 / 方法

- 主要字段、方法或协议值：`pdfEventContextSeconds`、`updatePdfEventContextSeconds`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`

## 主要调用点

打印设置页保存、主页初始化回读打印偏好。

## 注意事项

函数内部使用 `coerceIn(10, 300)`；持久化保存/读取也会限制到 10-300。当前 PDF 渲染固定按 15 秒连续波形切段，不再根据 `PrintSetting.pdfEventContextSeconds` 调整窗口长度。

## 最小验证方式

`rg "updatePdfEventContextSeconds|pdfEventContextSeconds"`；`./gradlew :app:compileDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
