<!-- context-seed:start -->
# HistoryList

## 定位

- ID: `AND-HL`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/HistoryList.kt:72`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `HistoryList` 是 Android 端的历史记录列表组件。
- 从本地数据库加载历史采集记录，以列表形式展示。
- 支持点击某条记录跳转到详情页（`HistoryRecordDetailActivity`）。

## 调用链

- 在 `HistoryRecordsActivity.Content()` 中使用。
- 与 iOS 端的历史记录管理对应（iOS 尚未实现历史列表组件）。
<!-- context-seed:end -->
