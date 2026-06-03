# Gson

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L465）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/060-gson.md`。

## 实体定位

- 实体：Gson
- ID / 别名：`com.google.code.gson:gson:2.10.1`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：CO2WavePointData 压缩前 JSON 序列化和迁移解析

## 补充职责

JSON 序列化/反序列化库，用于波形数据压缩前 JSON 和迁移解析。

## 关键 ID / 别名

`com.google.code.gson:gson:2.10.1`

## 关键字段 / 方法

`com.google.code.gson:gson:2.10.1`。

## 主要调用点

`LocalStorageKit.kt` 使用 `Gson()`；`DatabaserMigration_FROM1_TO2.kt` 使用 `com.google.gson.Gson()` 解析旧数据。

## 注意事项

数据模型字段变更需兼容历史 JSON，否则迁移/恢复可能失败。

## 最小验证方式

`./gradlew :app:assembleDebug`；迁移需数据回归

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
