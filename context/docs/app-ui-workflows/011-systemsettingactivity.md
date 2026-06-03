# SystemSettingActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L85）。
领域：页面。

## 实体定位

- 实体：SystemSettingActivity
- ID / 别名：SYSTEM_CONFIG_PAGE, 系统设置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/SystemSettingActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：语言和屏幕常亮设置

## 补充职责

系统设置页，切换语言并保存偏好；设备信息为空时触发 initCapnoEasyConection 拉取模块信息。

## 关键 ID / 别名

SYSTEM_CONFIG_PAGE, 系统设置

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/SystemSettingActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

语言和屏幕常亮设置。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
