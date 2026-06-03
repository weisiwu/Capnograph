# BaseActivity.ShowToast

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`（L256）。
领域：UI 函数。

## 实体定位

- 实体：`BaseActivity.ShowToast`
- ID / 别名：toast overlay, Toast 弹层
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/BaseActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/Toast.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：根据 ViewModel 状态渲染 Toast

## 补充职责

根据 ViewModel 状态渲染 Toast。

## 关键 ID / 别名

toast overlay, Toast 弹层

## 关键字段 / 方法

- 主要实体或方法：`BaseActivity.ShowToast`
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/pages/BaseActivity.kt`, `app/src/main/java/com/wldmedical/capnoeasy/components/Toast.kt`

## 主要调用点

由 Activity.Content、BaseLayout.float 或其他组件组合调用；输入状态通常来自 AppStateModel。

## 注意事项

根据 ViewModel 状态渲染 Toast。

## 最小验证方式

./gradlew :app:assembleDebug；人工检查对应页面渲染和回调。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
