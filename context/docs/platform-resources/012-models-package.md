# models` package

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L40）。
领域：模块。
实体级上下文：`context/docs/platform-resources/012-models-package.md`。

## 实体定位

- 实体：`models` package
- ID / 别名：app state, 状态层
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：全局状态容器和唯一 ViewModel

## 补充职责

全局状态与唯一 ViewModel 层，`AppStateModel` 管理页面、记录、蓝牙、患者、打印、语言、弹层等状态。

## 关键 ID / 别名

app state, 状态层

## 关键字段 / 方法

关键实体/文件：CapnoEasyApplication.kt, CapnoEasyConstant.kt, components/*, kits/*, models/*, pages/*, ui/theme/*。

## 主要调用点

`AppStateModel` 使用 `@HiltViewModel`，并通过 StateFlow/MutableState 和 AppCompatDelegate 驱动 UI 与语言切换。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
