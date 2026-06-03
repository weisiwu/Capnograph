# SettingActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L80）。
领域：页面。

## 实体定位

- 实体：SettingActivity
- ID / 别名：SETTING_PAGE, 设置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/SettingActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：设置入口页

## 补充职责

设置入口页，SettingList 点击后跳转二级设置页，或执行校零、屏幕常亮、关机等设备动作。

## 关键 ID / 别名

SETTING_PAGE, 设置

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/SettingActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

设置入口页。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
