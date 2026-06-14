<!-- context-seed:start -->
# CO2DisplayParamModel

## 定位

- ID: `CL-CDPM`
- 类型: `class` (ObservableObject)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/DisplayConfigView.swift:5`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `CO2DisplayParamModel` 是显示参数配置的 ViewModel，继承 `ObservableObject`。
- 持有两个 `@Published` 属性：
  - `CO2Unit: CO2UnitType`：CO2 单位（mmHg / % / KPa）。
  - `CO2Scale: CO2ScaleEnum`：CO2 刻度范围（根据单位动态变化）。
- 初始化时接收 `initCO2Unit` 和 `initCO2Scale`。

## 调用链

- 在 `DisplayConfigView` 中通过 `@StateObject` 创建和持有。
- `CO2Scale` 会根据 `CO2Unit` 的变化自动切换可选项（通过 `.onReceive` 监听）。
- 单位变化时触发刻度列表和默认刻度的联动更新。

## 使用建议

- 不需要手动释放，由 SwiftUI 的 `@StateObject` 生命周期自动管理。
- 刻度联动逻辑在 `DisplayConfigView.onReceive(paramsModel.$CO2Unit)` 中实现。
<!-- context-seed:end -->
