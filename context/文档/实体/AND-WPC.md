<!-- context-seed:start -->
# wheelPickerConfig

## 定位

- ID: `AND-WPC`
- 类型: `data class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:13`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `wheelPickerConfig<E: BaseEnmu<V>, V>` 是 WheelPicker 组件所需的配置数据类。
- 包含 `title: String`（标题）、`items: List<E>`（可选项列表）、`defaultValue: E`（默认值）。
- 在 `CapnoEasyConstant.kt` 中预定义了 `co2UnitsObj` 和 `wfSpeedsObj` 两个实例。

## 使用建议

- 在 WheelPicker 组件中根据此配置渲染滚轮选择器。
<!-- context-seed:end -->
