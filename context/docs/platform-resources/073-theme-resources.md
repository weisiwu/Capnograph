# Theme resources

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L483）。
领域：资源。
实体级上下文：`context/docs/platform-resources/073-theme-resources.md`。

## 实体定位

- 实体：Theme resources
- ID / 别名：themes, colors, 主题资源
- 源文件：`app/src/main/res/values/themes.xml`, `app/src/main/res/values/colors.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：XML 主题和颜色资源

## 补充职责

XML Activity 主题和基础颜色资源。

## 关键 ID / 别名

themes, colors, 主题资源

## 关键字段 / 方法

`Theme.CapnoEasy`、`Theme.Splash`；`Theme.Splash` 设置透明窗口背景和全屏；`colors.xml` 包含 purple/teal/black/white/splash_background。

## 主要调用点

Manifest application 和 SplashActivity 使用 `@style/Theme.Splash`；图标/启动视觉由 Compose SplashScreen 绘制。

## 注意事项

XML theme 与 Compose theme 并存；启动窗口行为改动需真机检查首帧。

## 最小验证方式

`./gradlew :app:assembleDebug`；手动查看启动页

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
