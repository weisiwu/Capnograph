# CapnoEasyApplication.onCreate

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L247）。
领域：应用入口。

## 实体定位

- 实体：`CapnoEasyApplication.onCreate`
- ID / 别名：app init, Application 初始化
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：初始化 ErrorReporter/Bugly、数据库、备份 helper、Activity 生命周期计数和错误上报上下文

## 补充职责

初始化 ErrorReporter/Bugly、数据库、备份 helper、Activity 生命周期计数和错误上报上下文。

## 关键 ID / 别名

app init, Application 初始化

## 关键字段 / 方法

- 主要实体或方法：`CapnoEasyApplication.onCreate`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`

## 主要调用点

由系统启动或 Activity 生命周期调用，是页面初始化和全局状态的根入口。

## 注意事项

初始化 ErrorReporter/Bugly、数据库、备份 helper、Activity 生命周期计数和错误上报上下文；数据库初始化异常上报后抛出，备份启动异常按非致命异常上报。

## 最小验证方式

./gradlew :app:assembleDebug；冷启动 App 验证初始化路径。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
