# Compose theme

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L484）。
领域：UI 主题。
实体级上下文：`context/docs/platform-resources/074-compose-theme.md`。

## 实体定位

- 实体：Compose theme
- ID / 别名：Theme.kt, Color.kt, Type.kt, Compose 主题
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Theme.kt`, `app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Color.kt`, `app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Type.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：Compose Material 主题

## 补充职责

Compose Material3 主题、颜色和排版定义。

## 关键 ID / 别名

Theme.kt, Color.kt, Type.kt, Compose 主题

## 关键字段 / 方法

`CapnoEasyTheme(dynamicColor=true)`；Dark/Light ColorScheme 使用 Purple/PurpleGrey/Pink token；Typography 默认 bodyLarge。

## 主要调用点

Compose 页面通过 `CapnoEasyTheme` 包裹并消费 `MaterialTheme`。

## 注意事项

Android 12+ 默认 dynamic color，运行时色板可能不是 `Color.kt` 的静态值。

## 最小验证方式

`./gradlew :app:assembleDebug`；不同系统主题下查看页面

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
