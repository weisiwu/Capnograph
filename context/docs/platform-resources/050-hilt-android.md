# Hilt Android

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L455）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/050-hilt-android.md`。

## 实体定位

- 实体：Hilt Android
- ID / 别名：`com.google.dagger:hilt-android:2.51.1`, `hilt-android-compiler`
- 源文件：`build.gradle.kts`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：Application、BaseActivity、ViewModel 和 Kit 构造注入

## 补充职责

Hilt 运行时与 compiler 依赖。

## 关键 ID / 别名

`com.google.dagger:hilt-android:2.51.1`, `hilt-android-compiler`

## 关键字段 / 方法

`com.google.dagger:hilt-android:2.51.1`、`hilt-android-compiler:2.51.1`。

## 主要调用点

`CapnoEasyApplication` 使用 `@HiltAndroidApp`；`AppStateModel` 使用 `@HiltViewModel`；app 应用 Hilt 插件和 kapt。

## 注意事项

hotmeltprint 声明 Hilt runtime 但未配置 Hilt plugin/kapt；不要假设库模块有完整 Hilt 注入链。

## 最小验证方式

`./gradlew :app:kaptDebugKotlin`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
