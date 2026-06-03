# Range slider assets

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L488）。
领域：资源。
实体级上下文：`context/docs/platform-resources/078-range-slider-assets.md`。

## 实体定位

- 实体：Range slider assets
- ID / 别名：`both_range_left_thumb`, `both_range_right_thumb`, `oneside_range_thumb`, 滑块资源
- 源文件：`app/src/main/res/drawable/both_range_left_thumb.png`, `app/src/main/res/drawable/both_range_right_thumb.png`, `app/src/main/res/drawable/oneside_range_thumb.png`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：范围选择器滑块资源

## 补充职责

报警/参数范围选择器滑块图片。

## 关键 ID / 别名

`both_range_left_thumb`, `both_range_right_thumb`, `oneside_range_thumb`, 滑块资源

## 关键字段 / 方法

`both_range_left_thumb`、`both_range_right_thumb`、`oneside_range_thumb`。

## 主要调用点

`RangeSelector` 使用这些 thumb 资源绘制范围选择控件。

## 注意事项

替换资源需验证不同屏幕密度下触控目标和视觉对齐。

## 最小验证方式

`./gradlew :app:assembleDebug`; 手动检查报警设置页

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
