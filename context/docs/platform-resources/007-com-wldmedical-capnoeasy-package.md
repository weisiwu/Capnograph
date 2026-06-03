# com.wldmedical.capnoeasy` package

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L35）。
领域：模块。
实体级上下文：`context/docs/platform-resources/007-com-wldmedical-capnoeasy-package.md`。

## 实体定位

- 实体：`com.wldmedical.capnoeasy` package
- ID / 别名：app package, 主包
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：主应用 Kotlin 源码根包

## 补充职责

主应用 Kotlin 源码根包，承载 Application、常量、页面、组件、状态模型和业务 kit 分层。

## 关键 ID / 别名

app package, 主包

## 关键字段 / 方法

关键实体/文件：CapnoEasyApplication.kt, CapnoEasyConstant.kt, components/*, kits/*, models/*, pages/*, ui/theme/*。

## 主要调用点

Manifest 使用相对类名 `.CapnoEasyApplication`，包名与 app namespace/applicationId 对齐。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
