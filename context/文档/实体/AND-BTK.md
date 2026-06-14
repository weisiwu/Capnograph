<!-- context-seed:start -->
# BlueToothKit

## 定位

- ID: `AND-BTK`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt:112`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BlueToothKit` 是 Android 端的蓝牙管理核心类，使用 Dagger Hilt 注入。
- 支持 BLE（低功耗蓝牙）和经典蓝牙两种扫描+连接方式。
- **核心功能**：
  - `searchDevices()`: 同时启动 BLE 和经典蓝牙扫描。
  - `connectDevice()`: 连接指定蓝牙设备，自动发现 Service 和 Characteristic。
  - `sendContinuous()`: 发送连续数据采集命令，不断接收波形数据。
  - `fetchDeviceInfo()`: 轮询获取设备信息（硬件版本、序列号、OEM ID）。
  - `getSavedBLEDeviceAddress()` / `savePairedBLEDevice()`: 持久化配对设备信息。
  - `updateDisplayParams()`: 更新设备显示参数（CO2 单位、刻度）。
  - `updateAlertRange()`: 更新设备报警范围。
  - `updateNoBreathAndGasCompensation()`: 更新窒息时间和氧气补偿。
- 使用 `WeakHashMap<BluetoothDevice, BLEDeviceExtra>` 管理每个设备的扩展属性。

## 调用链

- 通过 `BlueToothKitManager` 单例初始化并获取实例。
- `BaseActivity` 和所有 Activity 通过 `blueToothKit` 属性访问。
- 数据更新通过 `MutableStateFlow` 暴露给 UI 层（`AppState` → `AppStateModel`）。

## 使用建议

- BlueToothKitManager 在 `BaseActivity.onCreate()` 中初始化，需蓝牙权限通过后才开始工作。
- 与 iOS 端的 `BluetoothManager` 对应。
<!-- context-seed:end -->
