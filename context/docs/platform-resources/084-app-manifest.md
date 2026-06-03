# App manifest

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L499）。
领域：清单。
实体级上下文：`context/docs/platform-resources/084-app-manifest.md`。

## 实体定位

- 实体：App manifest
- ID / 别名：permissions, activities, 应用清单
- 源文件：`app/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：主应用清单

## 补充职责

主应用 Manifest，声明权限、硬件特性、Application、Activity、locale、backup 和 Bugly metadata。

## 关键 ID / 别名

permissions, activities, 应用清单

## 关键字段 / 方法

权限含 Bluetooth、Location、Storage、Network；Application 为 `.CapnoEasyApplication`；icon/roundIcon 为 app logo；theme 为 `Theme.Splash`。

## 主要调用点

系统启动 SplashActivity launcher；Manifest 合并、权限申请、localeConfig、backup/data extraction 都由此入口生效。

## 注意事项

Bugly metadata version `v0.1.1.20250327.2101` 与 app `versionName=1.2` 不同。

## 最小验证方式

`./gradlew :app:processDebugMainManifest`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
