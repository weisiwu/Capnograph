# AndroidPdfViewer

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L467）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/062-androidpdfviewer.md`。

## 实体定位

- 实体：AndroidPdfViewer
- ID / 别名：`com.github.barteksc:android-pdf-viewer:3.2.0-beta.1`
- 源文件：`app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明，排除 `com.android.support`

## 补充职责

Android PDF 预览库，当前由 app 声明。

## 关键 ID / 别名

`com.github.barteksc:android-pdf-viewer:3.2.0-beta.1`

## 关键字段 / 方法

`com.github.barteksc:android-pdf-viewer:3.2.0-beta.1`，并排除 `com.android.support`。

## 主要调用点

源码未检出直接调用；当前 PDF 流程主要是导出/保存。

## 注意事项

启用 PDF 预览时需要补页面/组件映射和文件权限验证。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
