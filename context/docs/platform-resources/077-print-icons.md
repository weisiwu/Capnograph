# Print icons

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L487）。
领域：资源。
实体级上下文：`context/docs/platform-resources/077-print-icons.md`。

## 实体定位

- 实体：Print icons
- ID / 别名：`nav_print_btn`, `nav_print_stop_btn`, `print_pdf`, `print_ticket`, 打印图标
- 源文件：`app/src/main/res/drawable/nav_print_btn.png`, `app/src/main/res/drawable/nav_print_stop_btn.png`, `app/src/main/res/drawable/print_pdf.png`, `app/src/main/res/drawable/print_ticket.png`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：打印和 PDF UI 图片

## 补充职责

打印和 PDF 操作相关图片资源。

## 关键 ID / 别名

`nav_print_btn`, `nav_print_stop_btn`, `print_pdf`, `print_ticket`, 打印图标

## 关键字段 / 方法

`nav_print_btn`、`nav_print_stop_btn`、`print_pdf`、`print_ticket`。

## 主要调用点

`NavBar`、`ActionModal` 等打印/PDF 入口引用。

## 注意事项

改名需同步组件里的 `R.drawable.*` 引用。

## 最小验证方式

`./gradlew :app:assembleDebug`; `rg "nav_print|print_pdf|print_ticket" app/src/main/java`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
