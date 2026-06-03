# DataBinding BaseLibrary

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L447）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/042-databinding-baselibrary.md`。

## 实体定位

- 实体：DataBinding BaseLibrary
- ID / 别名：`androidx.databinding:baseLibrary:3.2.0-alpha11`
- 源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的 AndroidX databinding baseLibrary

## 补充职责

AndroidX DataBinding baseLibrary 依赖，当前由 app 声明。

## 关键 ID / 别名

`androidx.databinding:baseLibrary:3.2.0-alpha11`

## 关键字段 / 方法

`androidx.databinding:baseLibrary:3.2.0-alpha11`；alias `libs.androidx.baselibrary`。

## 主要调用点

app 未启用 `buildFeatures.dataBinding`；源码未检出 data binding 使用。

## 注意事项

看起来是声明型/历史依赖，移除前需回归编译。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
