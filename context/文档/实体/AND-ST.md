<!-- context-seed:start -->
# SettingType

## 定位

- ID: `AND-ST`
- 类型: `enum class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/components/SettingList.kt:37`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `SettingType(value: Int)` 定义了设置页菜单项的枚举。
- **枚举值**：ZERO(0, 蓝牙校零)、DISPLAY(1, 显示设置)、ALERT(2, 报警设置)、MODULE(3, 模块设置)、PRINT(4, 打印设置)、SYSTEM(5, 系统设置)、DEVICE_LIST(6, 设备列表)、HISTORY(7, 历史记录)、SHUTDOWN(8, 关机)。

## 调用链

- `SettingList` 组件根据此枚举渲染菜单项。
- 与 iOS 端的 `ConfigItemTypes` 枚举对应。
<!-- context-seed:end -->
