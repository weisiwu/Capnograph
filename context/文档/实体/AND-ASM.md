<!-- context-seed:start -->
# AppStateModel

## 定位

- ID: `AND-ASM`
- 类型: `class` (HiltViewModel)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt:227`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AppStateModel` 是 Android 端的 ViewModel，通过 Dagger Hilt 注入 `AppState` 单例。
- 为每个 `AppState` 状态属性提供对应的 `State<T>` 暴露和 `update*` 方法。
- **关键方法**：
  - `updateCurrentPage()`: 切换当前页面导航。
  - `updateCurrentTab()`: 切换底部 Tab。
  - `updateToastData(ToastData?)` / `updateAlertData(AlertData?)` / 等：管理全屏覆盖层。
  - `clearXData()`: 清除所有全屏覆盖层（Toast/Alert/Confirm/Loading/ActionModal）。
  - `updateTotalCO2WavedData()`: 记录波形数据（在 `isRecording` 为 true 时追加）。
  - `delSavedCO2WavedDataChunk()`: 清理内存中的波形数据块。
- 所有 `update*` 方法在设置新值前自动调用 `clearXData()` 确保覆盖层不冲突。

## 调用链

- 每个 Activity 通过 `ViewModelProvider(this)[AppStateModel::class.java]` 获取 ViewModel。
- `BaseActivity` 提供 `lateinit var viewModel: AppStateModel`，子类直接使用。
- 与 iOS 端的 `AppConfigManage` 对应。

## 使用建议

- 使用 `viewModel.*` 的 State 属性在 Compose UI 中收集数据流。
- 全屏覆盖层（Toast/Alert/Confirm/Loading）互斥，`clearXData()` 确保一次只显示一个。
<!-- context-seed:end -->
