<!-- context-seed:start -->
# BaseEnmu

## 定位

- ID: `AND-BE`
- 类型: `interface` (泛型)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:9`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `BaseEnmu<T>` 是 Android 端所有枚举类型的通用接口，提供 `val value: T` 属性。
- 被 `CO2_UNIT`、`CO2_SCALE`、`WF_SPEED` 等枚举实现，统一枚举值的访问方式。
- 用于 `wheelPickerConfig` 数据类中泛型约束 `E: BaseEnmu<V>`。

## 使用建议

- 添加新枚举时实现此接口以兼容 WheelPicker 等通用组件。
<!-- context-seed:end -->
