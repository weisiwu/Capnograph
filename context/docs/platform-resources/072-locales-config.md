# locales_config

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L482）。
领域：资源。
实体级上下文：`context/docs/platform-resources/072-locales-config.md`。

## 实体定位

- 实体：locales_config
- ID / 别名：app locales, 应用语言配置
- 源文件：`app/src/main/res/xml/locales_config.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：语言切换的 Locale 配置

## 补充职责

Android 13+ app language locale 配置。

## 关键 ID / 别名

app locales, 应用语言配置

## 关键字段 / 方法

`app/src/main/res/xml/locales_config.xml`；声明 `en` 和 `zh`。

## 主要调用点

Manifest `android:localeConfig="@xml/locales_config"`；`AppStateModel` 用 AppCompatDelegate 切换语言。

## 注意事项

新增语言必须同步资源目录、locale config 和语言设置 UI。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`；手动验证语言切换

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
