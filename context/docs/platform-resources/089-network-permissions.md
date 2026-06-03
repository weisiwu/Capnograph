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

Bugly/网络状态能力可能使用；当前 CrashReport 初始化被注释但 metadata 和权限仍在。

## 注意事项

如果继续不启用 CrashReport，可后续评估网络权限是否仍必要。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
