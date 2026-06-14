<!-- context-seed:start -->
# EtCo2Table

## 定位

- ID: `AND-ET`
- 类型: `@Composable function`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2Table.kt:253`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `EtCo2Table` 是 Android 端主页的实时数据信息表格。
- 显示 RR（呼吸率）、ETCO2（呼气末 CO2）、设备名称、设备 ID 等信息。
- 使用 `AttributeLine` 渲染每行数据，`Atribute` 定义每行的标签和取值逻辑。

## 调用链

- 在 `MainActivity.Content()` 中位于波形图下方。
- 与 iOS 端的 `TableView` 对应。
<!-- context-seed:end -->
