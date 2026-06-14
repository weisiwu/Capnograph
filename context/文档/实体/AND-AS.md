<!-- context-seed:start -->
# AppState

## 定位

- ID: `AND-AS`
- 类型: `class` (Singleton)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt:53`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AppState` 是 Android 端的全局应用状态单例类，通过 Dagger Hilt 注入（`@Singleton`）。
- 使用 Jetpack Compose 的 `MutableState` 管理所有状态，支持响应式 UI 更新。
- **关键状态组**：
  - **导航状态**: `currentPage`、`currentTab`。
  - **全屏覆盖层**: `toastData`、`alertData`、`confirmData`、`loadingData`、`showActionModal`。
  - **蓝牙**: `devices`、`connectType`、`isRecording`、`discoveredPeripherals`。
  - **实时数据**: `rr`、`etCO2`、`totalCO2WavedData`、`totalCO2WavedDataFlow`。
  - **设置**: `alertETCO2Range`、`alertRRRange`、`CO2Unit`、`CO2Scale`、`WFSpeed`、`asphyxiationTime`、`o2Compensation`、`airPressure`。
  - **系统**: `language`、`appVersion`、`firmVersion`、`hardwareVersion` 等设备信息。
  - **病人**: `patientName`、`patientGender`、`patientAge`、`patientID`、`patientDepartment`、`patientBedNumber`。
  - **PDF**: `pdfHospitalName`、`pdfReportName`、`isPDF`、`pdfTemplateMode`、`pdfWatermarkEnabled` 等。
- 与 iOS 端的 `AppConfigManage` + `BluetoothManager` 组合对应。

## 使用建议

- 不要直接持有 AppState 引用，通过 `AppStateModel`（ViewModel）统一访问和更新状态。
<!-- context-seed:end -->
