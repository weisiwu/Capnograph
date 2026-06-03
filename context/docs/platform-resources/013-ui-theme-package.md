# ui.theme` package

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L41）。
领域：模块。
实体级上下文：`context/docs/platform-resources/013-ui-theme-package.md`。

## 实体定位

- 实体：`ui.theme` package
- ID / 别名：Compose theme, 主题层
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/ui/theme/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：Compose Material3 主题、颜色和字体定义

## 补充职责

Compose Material3 主题层，定义 `CapnoEasyTheme`、浅/深色 ColorScheme 和 Typography。

## 关键 ID / 别名

Compose theme, 主题层

## 关键字段 / 方法

关键实体/文件：CapnoEasyTheme, DarkColorScheme, LightColorScheme, Typography, Purple80/Purple40 color tokens。

## 主要调用点

Android 12+ 默认启用 dynamic color，实际运行色值可能被系统动态色覆盖。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
