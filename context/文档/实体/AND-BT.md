<!-- context-seed:start -->
# BluetoothType

## 定位

- ID: `AND-BT`
- 类型: `enum class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt:89`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BluetoothType` 定义了蓝牙搜索方式枚举。
- **BLE**: 低功耗蓝牙搜索。
- **CLASSIC**: 经典蓝牙搜索。
- **ALL**: 两种方式同时搜索。
- 在 `BlueToothKit.searchDevices()` 中根据当前设置使用。
<!-- context-seed:end -->
