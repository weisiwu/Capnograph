# Launcher icons

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L486）。
领域：资源。
实体级上下文：`context/docs/platform-resources/076-launcher-icons.md`。

## 实体定位

- 实体：Launcher icons
- ID / 别名：`ic_launcher`, `ic_launcher_round`, foreground/background, 启动图标
- 源文件：`app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`, `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`, `app/src/main/res/drawable/ic_launcher_foreground.xml`, `app/src/main/res/drawable/ic_launcher_background.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：启动器图标资源

## 补充职责

Android adaptive launcher icon 资源。

## 关键 ID / 别名

`ic_launcher`, `ic_launcher_round`, foreground/background, 启动图标

## 关键字段 / 方法

`mipmap-anydpi-v26/ic_launcher.xml`、`ic_launcher_round.xml`，foreground/background 位于 drawable。

## 主要调用点

当前 Manifest 直接使用 `@drawable/app_logo` 和 `@drawable/app_logo_round`，未使用 mipmap launcher icon。

## 注意事项

如果切换为 launcher icon，需要同步 Manifest 和不同密度/自适应图标检查。

## 最小验证方式

`./gradlew :app:assembleDebug`；安装后查看桌面图标

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
