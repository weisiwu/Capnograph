# AppStateModel.updateShowTrendingChart

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L167）。
领域：状态函数。

## 实体定位

- 实体：`AppStateModel.updateShowTrendingChart`
- ID / 别名：trend chart state, 趋势图开关
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：控制详情页趋势图是否展示；PDF 报告单仍固定输出全程趋势

## 补充职责

控制详情页趋势图是否展示；PDF 报告单仍固定输出全程 EtCO2 趋势，不再读取该开关决定 PDF 趋势图。

## 关键 ID / 别名

trend chart state, 趋势图开关

## 关键字段 / 方法

- 主要实体或方法：`AppStateModel.updateShowTrendingChart`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`, `app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt`

## 主要调用点

由页面、组件、蓝牙回调或本地存储回读调用，用于写入 AppState。

## 注意事项

控制详情页趋势图是否展示；PDF 报告单固定输出全程趋势和异常上下文波形。

## 最小验证方式

./gradlew :app:assembleDebug；rg 函数名确认调用点和状态副作用。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
