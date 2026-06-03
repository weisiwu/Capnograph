# LocalStorageKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L225）。
领域：存储。
聚合章节：Kit 与服务。

## 实体定位

- 实体：LocalStorageKit
- ID / 别名：local storage, Room, SharedPreferences, 本地存储
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：数据库、偏好设置、文件和记录持久化逻辑；打印偏好包含 PDF 模板/水印和异常上下文秒数配置

## 补充职责

本地数据服务，负责 Room、SharedPreferences、记录和打印偏好；打印偏好包含 PDF 模板模式、水印开关/文字/透明度和异常上下文秒数。

## 关键 ID / 别名

- 定位别名：local storage, Room, SharedPreferences, 本地存储
- 关键字段 / 方法：`database`、`currentRecordId`、`saveRecord`、`stopRecord`、`saveUserPrintSettingToPreferences`、`loadPrintSettingFromPreferences`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`database`、`currentRecordId`、`saveRecord`、`stopRecord`、`saveUserPrintSettingToPreferences`、`loadPrintSettingFromPreferences`、`KEY_PDF_TEMPLATE_MODE`、`KEY_PDF_WATERMARK_ENABLED`、`KEY_PDF_WATERMARK_TEXT`、`KEY_PDF_WATERMARK_OPACITY`、`KEY_PDF_EVENT_CONTEXT_SECONDS`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`

## 主要调用点

`BaseActivity` 初始化；主页/详情/设置页调用。

## 注意事项

`records`/`patients` 内存列表只局部更新，Room 是事实来源。

## 最小验证方式

`rg "LocalStorageKitManager|saveRecord"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
