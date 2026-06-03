# Alert sounds

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L490）。
领域：音频。
实体级上下文：`context/docs/platform-resources/080-alert-sounds.md`。

## 实体定位

- 实体：Alert sounds
- ID / 别名：`low_level_alert`, `middle_level_alert`, 报警音
- 源文件：`app/src/main/res/raw/low_level_alert.wav`, `app/src/main/res/raw/middle_level_alert.wav`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：报警音频资源

## 补充职责

报警音频资源。

## 关键 ID / 别名

`low_level_alert`, `middle_level_alert`, 报警音

## 关键字段 / 方法

`raw/low_level_alert.wav`、`raw/middle_level_alert.wav`。

## 主要调用点

`AlertAudioKit` 通过 `R.raw.*` 播放低/中等级报警音。

## 注意事项

音频行为需真机验证音量、循环、音频焦点和生命周期释放。

## 最小验证方式

`./gradlew :app:assembleDebug`; 真机触发报警

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
