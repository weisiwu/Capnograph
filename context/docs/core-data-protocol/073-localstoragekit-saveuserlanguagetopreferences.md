# LocalStorageKit.saveUserLanguageToPreferences

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L327）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.saveUserLanguageToPreferences`
- ID / 别名：save language pref, 保存语言偏好
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将 `zh`/`en` 写入 SharedPreferences

## 补充职责

保存语言偏好。

## 关键 ID / 别名

- 定位别名：save language pref, 保存语言偏好
- 关键字段 / 方法：`KEY_LANGUAGE = "userLanguage"`、默认 `zh`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`KEY_LANGUAGE = "userLanguage"`、默认 `zh`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

系统设置页。

## 注意事项

`language` 参数非 nullable，`?: remove` 分支实际不可达。

## 最小验证方式

检查函数签名

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
