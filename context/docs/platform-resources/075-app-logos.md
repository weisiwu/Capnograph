# App logos

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L485）。
领域：资源。
实体级上下文：`context/docs/platform-resources/075-app-logos.md`。

## 实体定位

- 实体：App logos
- ID / 别名：`app_logo`, `app_logo_round`, `wld_logo`, `logo`, 应用 logo, hotmelt logo
- 源文件：`app/src/main/res/drawable/app_logo.webp`, `app/src/main/res/drawable/app_logo_round.webp`, `app/src/main/res/drawable/wld_logo.png`, `hotmeltprint/src/main/res/drawable/logo.png`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：品牌资源

## 补充职责

应用和热敏打印模块品牌 logo 资源。

## 关键 ID / 别名

`app_logo`, `app_logo_round`, `wld_logo`, `logo`, 应用 logo, hotmelt logo

## 关键字段 / 方法

`app_logo.webp`、`app_logo_round.webp`、`wld_logo.png`、`hotmeltprint` 模块的 `logo.png`。

## 主要调用点

Manifest `android:icon`/`roundIcon` 使用 app logo；Splash 页面使用 `wld_logo`；`hotmeltprint` 的 `logo.png` 随 library resources 合入 app。

## 注意事项

替换图像需检查桌面图标、最近任务和启动页展示。Android 资源文件扩展名必须与真实格式一致；`hotmeltprint/src/main/res/drawable/logo.png` 需要保持真实 PNG，否则 release 资源合并会失败。

## 最小验证方式

`file hotmeltprint/src/main/res/drawable/logo.png`；`./gradlew :app:assembleDebug :app:assembleRelease`；安装后查看图标和启动页

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
