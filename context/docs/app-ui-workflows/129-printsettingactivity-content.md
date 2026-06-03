# PrintSettingActivity.Content

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L271）。
领域：UI 函数。

## 实体定位

- 实体：`PrintSettingActivity.Content`
- ID / 别名：print settings content, 打印设置内容, PDF 水印配置, PDF 异常上下文
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：保存医院名、报告名、PDF 模板/水印、异常上下文秒数、输出类型和详情趋势图开关

## 补充职责

保存医院名、报告名、PDF 正式/调试模板、水印开关/文字/透明度、异常片段上下文秒数、输出类型和详情趋势图开关。

## 关键 ID / 别名

print settings content, 打印设置内容, PDF 水印配置, PDF 异常上下文

## 关键字段 / 方法

- 主要实体或方法：`PrintSettingActivity.Content`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`

## 主要调用点

由 Activity.Content、BaseLayout.float 或其他组件组合调用；输入状态通常来自 AppStateModel。

## 注意事项

保存时同步更新 ViewModel 和 SharedPreferences；水印透明度输入按 Float 解析并裁剪到 `0..1`，解析失败时使用默认 `0.3`。异常上下文秒数输入按 Int 解析并裁剪到 `10..300`，解析失败时使用默认 `60`。

## 最小验证方式

./gradlew :app:assembleDebug；人工检查对应页面渲染和回调。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
