<!-- context-seed:start -->
# Device

## 定位

- ID: `AND-DEV`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/DeviceList.kt:42`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `Device` 是蓝牙设备的数据模型。
- 封装蓝牙设备的名称、地址、RSSI 等信息。
- 用于 `AppState.devices` 列表和 `DeviceList` 组件渲染。

## 调用链

- `BlueToothKit.discoveredPeripherals` 扫描时创建 Device 实例。
- `DeviceList` 根据此数据渲染设备列表项。
<!-- context-seed:end -->
