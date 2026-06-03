# SimSun font

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L491）。
领域：资源。
实体级上下文：`context/docs/platform-resources/081-simsun-font.md`。

## 实体定位

- 实体：SimSun font
- ID / 别名：`SimSun.ttf`, 宋体字体
- 源文件：`app/src/main/assets/fonts/SimSun.ttf`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：App/报告使用的字体资源

## 补充职责

宋体字体资产，用于中文报告/PDF 字体需求。

## 关键 ID / 别名

`SimSun.ttf`, 宋体字体

## 关键字段 / 方法

`app/src/main/assets/fonts/SimSun.ttf`，约 10M。

## 主要调用点

PDF/报告能力可从 assets 加载该字体，保障中文渲染。

## 注意事项

替换字体需验证 PDF 中文、体积和授权合规。

## 最小验证方式

`./gradlew :app:assembleDebug`; 手动导出 PDF 检查中文

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
