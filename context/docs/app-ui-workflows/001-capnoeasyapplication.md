# CapnoEasyApplication

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L75）。
领域：应用入口。

## 实体定位

- 实体：CapnoEasyApplication
- ID / 别名：Application, Hilt app, 应用入口
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：全局 Application 入口

## 补充职责

Hilt Application 入口，持有全局 Context、Room AppDatabase、DatabaseBackupHelper，并通过 ActivityLifecycleCallbacks 维护 activityCount。

## 关键 ID / 别名

Application, Hilt app, 应用入口

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`

## 主要调用点

由系统启动或 Activity 生命周期调用，是页面初始化和全局状态的根入口。

## 注意事项

全局 Application 入口。

## 最小验证方式

./gradlew :app:assembleDebug；冷启动 App 验证初始化路径。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
