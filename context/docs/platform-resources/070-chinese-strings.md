# Chinese strings

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L480）。
领域：资源。
实体级上下文：`context/docs/platform-resources/070-chinese-strings.md`。

## 实体定位

- 实体：Chinese strings
- ID / 别名：`values/strings.xml`, 中文文案
- 源文件：`app/src/main/res/values/strings.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：默认字符串资源

## 补充职责

默认中文字符串资源和 `R.string.*` key 来源。

## 关键 ID / 别名

`values/strings.xml`, 中文文案

## 关键字段 / 方法

`values/strings.xml`；`app_name=CapnoEasy`；`app_version=v1.0.0`；包含页面标题、设置、报警、PDF、历史、表格、导航等 key。

## 主要调用点

页面、组件、PDFKit、LocalStorageKit 等通过 `R.string.*` 读取。

## 注意事项

key 改名会破坏编译；中英文资源 key 应保持对齐。

## 最小验证方式

`./gradlew :app:assembleDebug`；必要时 `rg "R.string.<key>" app/src/main/java`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
