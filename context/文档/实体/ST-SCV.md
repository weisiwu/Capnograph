<!-- context-seed:start -->
# SystemConfigView

## 定位

- ID: `ST-SCV`
- 类型: `struct` (View)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/SystemConfigView.swift:85`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SystemConfigView` 是 iOS 端的系统设置页面，展示设备信息和语言切换。
- 通过 `BaseConfigContainerView(configType: ConfigItemTypes.System)` 包裹。
- 列表内容（通过 `InfoItem` 渲染）：
  - 系统语言（Radio 模式，中/英文切换）
  - 固件版本 (`appConfigManage.firmwareVersion`)
  - 硬件版本 (`appConfigManage.hardwareVersion`)
  - 软件版本 (`appConfigManage.softwareVersion`)
  - 生产日期 (`appConfigManage.productionDate`)
  - 序列号 (`appConfigManage.serialNumber`)
  - 模块名称 (`appConfigManage.ModuleName`)
- 进入页面时自动通过 `bluetoothManager.getDeviceInfo()` 从设备获取硬件信息。

## 调用链

- 从 `ConfigView` 导航栏"系统设置"按钮推送进入。
- 调用 `bluetoothManager.getDeviceInfo(cb: getSettingInfoCallback)` 获取设备信息。
- 回调根据 `ISBState` 类型分发：`CMD_84H` 获取出厂信息、`CMD_CAH` 获取传感器信息。

## 使用建议

- 设备未连接时显示默认值 "--"。
- 语言切换实时生效，需确保所有文本已通过 `getTextByKey()` 本地化。
<!-- context-seed:end -->
