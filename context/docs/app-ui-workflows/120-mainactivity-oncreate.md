# MainActivity.onCreate

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L262）。
领域：页面函数。

## 实体定位

- 实体：`MainActivity.onCreate`
- ID / 别名：main init, 主页初始化
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：自动连接历史设备、申请文件权限、读取打印偏好并扫描打印机

## 补充职责

自动连接历史设备、申请文件权限、读取打印偏好并扫描打印机。

## 关键 ID / 别名

main init, 主页初始化

## 关键字段 / 方法

- 主要实体或方法：`MainActivity.onCreate`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt`

## 主要调用点

由对应 Activity 生命周期、导航栏按钮、保存按钮或 Compose 内容回调触发。

## 注意事项

自动连接历史设备、申请文件权限、读取打印偏好并扫描打印机。

## 最小验证方式

./gradlew :app:assembleDebug；人工触发对应按钮/生命周期路径。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
