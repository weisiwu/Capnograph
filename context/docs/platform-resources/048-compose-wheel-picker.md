# Compose Wheel Picker

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L453）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/048-compose-wheel-picker.md`。

## 实体定位

- 实体：Compose Wheel Picker
- ID / 别名：`com.github.zj565061763:compose-wheel-picker:1.0.0-rc02`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/components/WheelPicker.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：WheelPicker 组件使用 `FVerticalWheelPicker`

## 补充职责

第三方滚轮选择器，支撑项目内 WheelPicker 组件。

## 关键 ID / 别名

`com.github.zj565061763:compose-wheel-picker:1.0.0-rc02`

## 关键字段 / 方法

`com.github.zj565061763:compose-wheel-picker:1.0.0-rc02`。

## 主要调用点

`components/WheelPicker.kt` 使用 `FVerticalWheelPicker`。

## 注意事项

升级后需验证设置页滚轮默认值、滚动选择和回调同步。

## 最小验证方式

`./gradlew :app:assembleDebug`；手动验证滚轮组件

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
