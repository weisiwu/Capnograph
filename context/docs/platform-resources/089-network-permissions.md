# Network permissions

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L504）。
领域：清单。
实体级上下文：`context/docs/platform-resources/089-network-permissions.md`。

## 实体定位

- 实体：Network permissions
- ID / 别名：INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE, 网络权限
- 源文件：`app/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：网络权限声明

## 补充职责

网络和网络状态权限声明。

## 关键 ID / 别名

INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE, 网络权限

## 关键字段 / 方法

`INTERNET`、`ACCESS_NETWORK_STATE`、`ACCESS_WIFI_STATE`。

## 主要调用点

Bugly CrashReport 通过 `ErrorReporter` 启用，崩溃与非致命异常上报需要网络权限；网络状态权限供 SDK 判断上传环境。

## 注意事项

如果未来移除 Bugly 或改为离线上报，可重新评估 `INTERNET`、`ACCESS_NETWORK_STATE`、`ACCESS_WIFI_STATE` 的必要性。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
