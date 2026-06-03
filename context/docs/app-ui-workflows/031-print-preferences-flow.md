# Print preferences flow

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L123）。
领域：设置。

## 实体定位

- 实体：Print preferences flow
- ID / 别名：report settings, 输出类型, 打印设置, PDF 水印配置
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：保存医院名、报告名、PDF/热敏输出、PDF 模板/水印配置、异常波形上下文秒数和详情趋势图选项到 SharedPreferences

## 补充职责

保存医院名、报告名、PDF/热敏输出、PDF 模板/水印配置、异常波形上下文秒数和详情趋势图选项到 SharedPreferences。

## 关键 ID / 别名

report settings, 输出类型, 打印设置, PDF 水印配置

## 关键字段 / 方法

- 主要实体或方法：见源码中同名类、数据模型、常量或 Composable 声明。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

由二级设置页保存按钮触发，同时可能写 ViewModel、本地偏好和设备参数。

## 注意事项

打印设置页保存水印透明度前会裁剪到 `0..1`；正式模板默认关闭水印，调试模板默认启用水印，用户保存的开关会覆盖模板默认值。异常波形上下文秒数默认 `60`，保存和读取时裁剪到 `10..300`，但当前 PDF 导出固定按 15 秒连续波形切段，不再使用该值。

## 最小验证方式

./gradlew :app:assembleDebug；人工保存对应设置并重开页面确认状态。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
