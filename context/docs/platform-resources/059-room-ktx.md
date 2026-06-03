# Room KTX

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L464）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/059-room-ktx.md`。

## 实体定位

- 实体：Room KTX
- ID / 别名：`androidx.room:room-ktx:2.5.2`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`app/data_version_list.txt`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Room 协程/Flow 支持

## 补充职责

Room 协程/Flow 扩展支持。

## 关键 ID / 别名

`androidx.room:room-ktx:2.5.2`

## 关键字段 / 方法

`androidx.room:room-ktx:2.5.2`。

## 主要调用点

`LocalStorageKit` 的数据库访问与异步/协程能力配套使用。

## 注意事项

数据库查询行为变更需结合 UI 历史记录和记录保存流程验证。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
