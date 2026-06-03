# Location permissions

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L502）。
领域：清单。
实体级上下文：`context/docs/platform-resources/087-location-permissions.md`。

## 实体定位

- 实体：Location permissions
- ID / 别名：ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, 定位权限
- 源文件：`app/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：部分 Android 版本蓝牙扫描需要的定位权限

## 补充职责

定位权限，主要兼容旧版本 BLE 扫描要求。

## 关键 ID / 别名

ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, 定位权限

## 关键字段 / 方法

`ACCESS_FINE_LOCATION`、`ACCESS_COARSE_LOCATION`、`ACCESS_BACKGROUND_LOCATION`。

## 主要调用点

旧 Android BLE 扫描可能依赖定位授权；后台定位由 Manifest 声明。

## 注意事项

`BLUETOOTH_SCAN neverForLocation` 不替代旧版本定位权限；后台定位上架审核敏感。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`; 真机扫描验证

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
