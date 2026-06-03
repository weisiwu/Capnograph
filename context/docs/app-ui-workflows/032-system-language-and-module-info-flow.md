# System language and module info flow

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L124）。
领域：设置。

## 实体定位

- 实体：System language and module info flow
- ID / 别名：language, system info, 系统设置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/SystemSettingActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：切换应用语言，读取 App 版本和模块硬件/软件/序列号信息

## 补充职责

切换应用语言，读取 App 版本和模块硬件/软件/序列号信息。

## 关键 ID / 别名

language, system info, 系统设置

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/SystemSettingActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`

## 主要调用点

由二级设置页保存按钮触发，同时可能写 ViewModel、本地偏好和设备参数。

## 注意事项

切换应用语言，读取 App 版本和模块硬件/软件/序列号信息。

## 最小验证方式

./gradlew :app:assembleDebug；人工保存对应设置并重开页面确认状态。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
