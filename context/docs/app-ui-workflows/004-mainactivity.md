# MainActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L78）。
领域：页面。

## 实体定位

- 实体：MainActivity
- ID / 别名：home, HOME_PAGE, 主页, 波形主页
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：CO2 波形、数据表格和记录流程

## 补充职责

主页 Activity，负责自动连接历史 BLE 设备、请求文件管理权限、恢复打印偏好、默认扫描打印机，并管理开始/停止记录。打印偏好回读会同步 PDF 输出类型、模板、水印、异常波形上下文秒数、患者字段和详情趋势图开关到 ViewModel；自动连接、偏好读取、扫描、开始/停止记录和权限恢复异常会进入 ErrorReporter。

## 关键 ID / 别名

home, HOME_PAGE, 主页, 波形主页

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

CO2 波形、数据表格和记录流程；错误上报 metadata 不直接携带患者姓名/住院号/床号。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
