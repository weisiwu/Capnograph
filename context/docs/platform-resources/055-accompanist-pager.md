# Accompanist Pager

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L460）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/055-accompanist-pager.md`。

## 实体定位

- 实体：Accompanist Pager
- ID / 别名：`accompanist-pager:0.28.0`, `accompanist-pager-indicators:0.28.0`
- 源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：EtCo2LineChart 使用 HorizontalPager 和 PagerIndicator

## 补充职责

Accompanist pager 与 indicator，支撑实时波形多设备轮播。

## 关键 ID / 别名

`accompanist-pager:0.28.0`, `accompanist-pager-indicators:0.28.0`

## 关键字段 / 方法

`com.google.accompanist:accompanist-pager:0.28.0`、`accompanist-pager-indicators:0.28.0`。

## 主要调用点

`EtCo2LineChart.kt` 使用 `HorizontalPager`、`HorizontalPagerIndicator`、`rememberPagerState`。

## 注意事项

Pager API 标注 experimental；升级/迁移到 Compose Foundation Pager 需回归波形翻页和 indicator。

## 最小验证方式

`./gradlew :app:assembleDebug`；手动验证实时波形页

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
