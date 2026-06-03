# SearchActivity

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L79）。
领域：页面。

## 实体定位

- 实体：SearchActivity
- ID / 别名：devices, DEVICES_LIST_PAGE, 设备, 设备搜索, 附近设备
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/SearchActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：BLE 和经典蓝牙扫描/连接页面

## 补充职责

设备页 Activity，展示 BluetoothDevice 列表，发起扫描、连接设备、保存配对地址并回到主页。

## 关键 ID / 别名

devices, DEVICES_LIST_PAGE, 设备, 设备搜索, 附近设备

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/SearchActivity.kt`

## 主要调用点

由 AndroidManifest/Intent 或 BaseLayout Tab 跳转进入；生命周期内通过 BaseActivity 共享 ViewModel 和 Kit。

## 注意事项

BLE 和经典蓝牙扫描/连接页面。

## 最小验证方式

./gradlew :app:assembleDebug；人工打开对应 Activity 流程。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
