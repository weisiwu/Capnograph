# PrintSettingActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L84）。
领域：页面。

## 实体定位

- 实体：PrintSettingActivity
- ID / 别名：PRINT_CONFIG_PAGE, 打印设置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：PDF 和热敏打印设置

## 补充职责

打印设置页，编辑医院名/报告名，用 TypeSwitch 控制 PDF/热敏输出和详情页趋势图展示，并写入 SharedPreferences。

## 关键 ID / 别名

PRINT_CONFIG_PAGE, 打印设置

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

PDF 和热敏打印设置。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
