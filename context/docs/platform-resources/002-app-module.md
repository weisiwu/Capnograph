# app module

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L25）。
领域：模块。
实体级上下文：`context/docs/platform-resources/002-app-module.md`。

## 实体定位

- 实体：app module
- ID / 别名：`:app`, `com.wldmedical.capnoeasy`, 主应用模块
- 源文件：`app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 关联总览文档：`context/docs/platform-resources.md`
- 备注：主应用模块

## 补充职责

主 Android application 模块，包含 BLE 设备连接、实时 CO2 波形、记录、设置、历史、PDF 导出和热敏打印入口。

## 关键 ID / 别名

`:app`, `com.wldmedical.capnoeasy`, 主应用模块

## 关键字段 / 方法

`namespace`/`applicationId` 均为 `com.wldmedical.capnoeasy`；`compileSdk=35`、`minSdk=30`、`targetSdk=35`、`versionCode=3`、`versionName="1.2"`；启用 Compose、Hilt、kapt。

## 主要调用点

`app/src/main/AndroidManifest.xml` 绑定 `.CapnoEasyApplication`、Splash launcher 和页面 Activity；依赖 `project(":hotmeltprint")` 输出热敏打印能力。

## 注意事项

release 构建开启 `isMinifyEnabled=true` 和 `isShrinkResources=true`；Manifest 里也写了 `android:versionName="1.2"`，版本变更需双处核对。

## 最小验证方式

`./gradlew :app:assembleDebug`，Manifest 变更可用 `./gradlew :app:processDebugMainManifest`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
