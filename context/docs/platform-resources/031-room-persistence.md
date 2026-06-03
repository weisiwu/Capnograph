# Room persistence

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L64）。
领域：存储平台。
实体级上下文：`context/docs/platform-resources/031-room-persistence.md`。

## 实体定位

- 实体：Room persistence
- ID / 别名：`androidx.room`, `room-runtime=2.5.2`, `room-compiler=2.5.2`, `room-ktx=2.5.2`, `room.schemaLocation`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Room 运行时、compiler、KTX 和 schema 导出路径

## 补充职责

app 本地数据库能力，持久化患者、记录和 CO2 波形数据。

## 关键 ID / 别名

`androidx.room`, `room-runtime=2.5.2`, `room-compiler=2.5.2`, `room-ktx=2.5.2`, `room.schemaLocation`

## 关键字段 / 方法

Room runtime/compiler/ktx 均为 `2.5.2`；schema 输出目录为 `app/schemas`；`LocalStorageKit.kt` 定义 `AppDatabase : RoomDatabase`。

## 主要调用点

`LocalStorageKit`、`DatabaseBackupHelperKit` 和历史记录/PDF 工作流依赖数据库。

## 注意事项

数据库结构变更必须同步 migration、schema、`app/data_version_list.txt` 和 core-data-protocol 文档。

## 最小验证方式

`./gradlew :app:kaptDebugKotlin`，数据库升级还需 migration 测试。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
