# PageScene.PRINT_CONFIG_PAGE

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L100）。
领域：常量。

## 实体定位

- 实体：PageScene.PRINT_CONFIG_PAGE
- ID / 别名：constant_print_setting, 打印设置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：打印设置页标题

## 补充职责

打印设置页标题。

## 关键 ID / 别名

constant_print_setting, 打印设置

## 关键字段 / 方法

- 主要实体或方法：PageScene.PRINT_CONFIG_PAGE
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

由页面标题、导航、Intent extra 或设置页控件读取。

## 注意事项

打印设置页标题。

## 最小验证方式

./gradlew :app:assembleDebug；rg 常量名确认所有引用。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
