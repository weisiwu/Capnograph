<!-- context-seed:start -->
# LocalStorageKit

## 定位

- ID: `AND-LSK`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LocalStorageKit` 是 Android 端的本地存储工具类。
- 使用 Room 数据库存储实体（`SystemSettingEntity`、`PatientEntity`、`PrintSettingEntity`）。
- **核心功能**：
  - 增删改查历史记录数据。
  - 保存/加载用户语言偏好。
  - 保存/加载打印设置。
  - 管理 Room 数据库（`AppDatabase`）。
- 通过 `LocalStorageKitManager` 单例获取实例。

## 使用建议

- 所有数据持久化操作通过此 Kit 统一管理。
- 实体通过 Room @Entity 和 @Dao 注解定义。
<!-- context-seed:end -->
