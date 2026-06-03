# pages` package

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L36）。
领域：模块。
实体级上下文：`context/docs/platform-resources/008-pages-package.md`。

## 实体定位

- 实体：`pages` package
- ID / 别名：Activity pages, 页面层
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：页面 Activity 和页面级工作流

## 补充职责

Activity 页面层，负责启动、主页、设备搜索、设置、历史详情和各参数配置页面。

## 关键 ID / 别名

Activity pages, 页面层

## 关键字段 / 方法

关键实体/文件：SplashActivity, BaseActivity, MainActivity, SearchActivity, SettingActivity, AlertSettingActivity, DisplaySettingActivity, HistoryRecordDetailActivity, HistoryRecordsActivity, ModuleSettingActivity, PrintSettingActivity, SystemSettingActivity。

## 主要调用点

页面间导航主要使用 Intent；多数页面继承 `BaseActivity` 并共享 BaseLayout/弹层/权限逻辑。

## 注意事项

包级文档只描述层级边界；类/函数级行为以 app-ui-workflows 或 core-data-protocol 的实体文档为准。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
