# LocalStorageKit.loadUserLanguageFromPreferences

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L328）。
领域：存储函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`LocalStorageKit.loadUserLanguageFromPreferences`
- ID / 别名：load language pref, 读取语言偏好
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：启动时读取语言偏好，默认中文

## 补充职责

读取语言偏好。

## 关键 ID / 别名

- 定位别名：load language pref, 读取语言偏好
- 关键字段 / 方法：`LanguageTypes.ENGLISH`、`LanguageTypes.CHINESE`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`LanguageTypes.ENGLISH`、`LanguageTypes.CHINESE`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

启动/系统设置。

## 注意事项

只有 `"en"` 返回英文，其他默认中文。

## 最小验证方式

检查条件

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
