# Room Runtime

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L462）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/057-room-runtime.md`。

## 实体定位

- 实体：Room Runtime
- ID / 别名：`androidx.room:room-runtime:2.5.2`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Room 数据库运行时

## 补充职责

Room 数据库运行时。

## 关键 ID / 别名

`androidx.room:room-runtime:2.5.2`

## 关键字段 / 方法

`androidx.room:room-runtime:2.5.2`。

## 主要调用点

`LocalStorageKit.kt` 中 `AppDatabase : RoomDatabase`、DAO/entity 持久化使用。

## 注意事项

数据库字段变更需同步迁移、schema 和 core-data-protocol 文档。

## 最小验证方式

`./gradlew :app:kaptDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
