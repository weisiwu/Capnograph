# AndroidX Bluetooth

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L450）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/045-androidx-bluetooth.md`。

## 实体定位

- 实体：AndroidX Bluetooth
- ID / 别名：`androidx.bluetooth:bluetooth:1.0.0-alpha02`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明；当前 BlueToothKit 主要直接使用 Android framework Bluetooth API

## 补充职责

AndroidX Bluetooth alpha 依赖，当前由 app 声明。

## 关键 ID / 别名

`androidx.bluetooth:bluetooth:1.0.0-alpha02`

## 关键字段 / 方法

`androidx.bluetooth:bluetooth:1.0.0-alpha02`。

## 主要调用点

`BlueToothKit` 当前主要直接使用 Android framework Bluetooth API。

## 注意事项

BLE 行为变更不能只靠编译，需要 Android 12+ 运行时权限和真机设备验证。

## 最小验证方式

`./gradlew :app:assembleDebug`；蓝牙改动需真机验证

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
