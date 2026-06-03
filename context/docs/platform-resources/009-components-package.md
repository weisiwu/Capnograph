# components` package

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L37）。
领域：模块。
实体级上下文：`context/docs/platform-resources/009-components-package.md`。

## 实体定位

- 实体：`components` package
- ID / 别名：Compose components, 组件层
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：可复用 Compose 组件和组件状态模型

## 补充职责

可复用 Compose UI 组件层，承载顶栏、底栏、实时表格、波形图、弹窗、Toast、选择器和设置列表。

## 关键 ID / 别名

Compose components, 组件层

## 关键字段 / 方法

关键实体/文件：ActionBar, ActionModal, AlertModal, BaseLayout, ConfirmModal, CustomTextField, DeviceList, EtCo2LineChart, EtCo2Table, HistoryList, Loading, NavBar, RangeSelector, SaveButton, SettingList, Toast, TypeSwitch, WheelPicker。

## 主要调用点

组件大量消费 `AppStateModel`、`R.string.*`、`R.drawable.*`；`EtCo2LineChart` 使用 AndroidView 包装 MPAndroidChart。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
