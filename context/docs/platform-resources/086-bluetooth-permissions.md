# Bluetooth permissions

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L501）。
领域：清单。
实体级上下文：`context/docs/platform-resources/086-bluetooth-permissions.md`。

## 实体定位

- 实体：Bluetooth permissions
- ID / 别名：BLUETOOTH, BLUETOOTH_CONNECT, BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, 蓝牙权限
- 源文件：`app/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：蓝牙权限声明

## 补充职责

蓝牙扫描、连接、广播和经典蓝牙能力权限声明。

## 关键 ID / 别名

BLUETOOTH, BLUETOOTH_CONNECT, BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, 蓝牙权限

## 关键字段 / 方法

`BLUETOOTH`、`BLUETOOTH_ADMIN`、`BLUETOOTH_CONNECT`、`BLUETOOTH_SCAN` with `neverForLocation`、`BLUETOOTH_ADVERTISE`；feature `bluetooth`/`bluetooth_le` required true。

## 主要调用点

`BaseActivity.checkBluetoothPermissions`、`SearchActivity`、`BlueToothKit`、`HotmeltPinter` 蓝牙打印/连接流程。

## 注意事项

Android 12+ 需要运行时蓝牙权限；设备 feature required=true 会限制无蓝牙设备安装/可见性。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`; Android 12+ 真机权限验证

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
