# Device/UI icons

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L489）。
领域：资源。
实体级上下文：`context/docs/platform-resources/079-device-ui-icons.md`。

## 实体定位

- 实体：Device/UI icons
- ID / 别名：`device_tyoe_mark`, `empty_device_list`, `pull_up`, `m3_*`, `fail_icon`, 设备和通用 UI 图标
- 源文件：`app/src/main/res/drawable/device_tyoe_mark.png`, `app/src/main/res/drawable/empty_device_list.png`, `app/src/main/res/drawable/pull_up.png`, `app/src/main/res/drawable/m3_arrow_forward.png`, `app/src/main/res/drawable/m3_lightbulb.png`, `app/src/main/res/drawable/m3_power_settings.png`, `app/src/main/res/drawable/m3_refresh.png`, `app/src/main/res/drawable/fail_icon.png`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：通用 UI 图片资源

## 补充职责

设备列表、设置项、Toast、导航等通用 UI 图片。

## 关键 ID / 别名

`device_tyoe_mark`, `empty_device_list`, `pull_up`, `m3_*`, `fail_icon`, 设备和通用 UI 图标

## 关键字段 / 方法

`device_tyoe_mark`、`empty_device_list`、`pull_up`、`m3_arrow_forward`、`m3_lightbulb`、`m3_power_settings`、`m3_refresh`、`fail_icon`。

## 主要调用点

`DeviceList`、`HistoryList`、`TypeSwitch`、`SettingList`、`Toast`、`NavBar` 等组件引用。

## 注意事项

`device_tyoe_mark` 文件名存在 typo，源码引用也沿用 typo；改正需同步资源和引用。

## 最小验证方式

`./gradlew :app:assembleDebug`; `rg "device_tyoe_mark|empty_device_list|m3_" app/src/main/java`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
