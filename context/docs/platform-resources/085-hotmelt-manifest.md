# Hotmelt manifest

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L500）。
领域：清单。
实体级上下文：`context/docs/platform-resources/085-hotmelt-manifest.md`。

## 实体定位

- 实体：Hotmelt manifest
- ID / 别名：printer library manifest, 打印库清单
- 源文件：`hotmeltprint/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：打印库清单

## 补充职责

热敏打印库 Manifest，目前为空壳。

## 关键 ID / 别名

printer library manifest, 打印库清单

## 关键字段 / 方法

`hotmeltprint/src/main/AndroidManifest.xml` 只声明根 `<manifest>`。

## 主要调用点

作为 Android library 参与 app Manifest 合并。

## 注意事项

打印权限不在库内声明，依赖 app Manifest 的蓝牙/存储相关权限。

## 最小验证方式

`./gradlew :hotmeltprint:processDebugManifest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
