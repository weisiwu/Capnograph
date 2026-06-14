<!-- context-seed:start -->
# AppConfigManage

## 定位

- ID: `CL-ACM`
- 类型: `class` (ObservableObject)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:239`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AppConfigManage` 是 iOS 端的全局应用配置管理类，继承 `ObservableObject`。
- 通过 `@Published` 属性驱动 UI 更新。
- **核心状态组**：
  - **界面覆盖层**：`loadingMessage`, `toastMessage`, `toastType`。
  - **显示参数**：`CO2Unit`, `CO2Scale`, `WFSpeed`。
  - **系统设置**：`language`, `firmwareVersion`, `hardwareVersion`, `softwareVersion`, `serialNumber`, `ModuleName`。
  - **患者信息**：`patientName`, `patientAge`, `patientGender` 等。
  - **PDF 设置**：`pdfHospitalName`, `pdfReportName`, `isPDF`。
- 提供 `getTextByKey(key: String) -> String` 实现多语言文本支持。
- 通过 `@EnvironmentObject` 注入到所有子视图中。

## 调用链

- 在 `CapnoGraphApp` 中通过 `@StateObject` 创建，通过 `.environmentObject()` 注入。
- 所有 View 通过 `@EnvironmentObject var appConfigManage: AppConfigManage` 访问。
- `listenToBluetoothManager()` 绑定蓝牙管理器，监听蓝牙状态变化。
- 与 Android 端的 `AppState` + `AppStateModel` 组合对应。
<!-- context-seed:end -->
