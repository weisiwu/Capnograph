# 临时任务列表 1 - 平台、依赖、资源实体补充

来源：`context/entity-id-mapping.md`。

并行约定：本文件中的任务与另外两个临时任务列表没有先后关系；每个实体只出现在一个列表中，可以同时执行。执行时只处理本列表实体，避免修改其他列表的目标补充文档。

建议补充位置：每个实体一个 Markdown，实体级文档目录为 `context/docs/platform-resources/`；`context/docs/platform-resources.md` 和 `context/docs/build-platform.md` 仅作为总览/索引。

范围：项目/模块、源码包、构建平台、三方依赖、资源、清单权限、测试入口。

实体任务数：94。

## 执行要求

- [x] 逐项读取任务里的 `源文件`，必要时读取 `当前补充上下文`。
- [x] 为实体补充职责、关键 ID/别名、关键字段/方法、主要调用点、注意事项和最小验证方式。
- [x] 如果发现定位表信息与代码不一致，以代码为准，并记录需要同步 `context/entity-id-mapping.md` 的行号。
- [x] 本列表完成后，只汇总本列表涉及的实体、目标文档和验证结果。

## 覆盖章节

- 项目与模块：6 个实体
- 源码包与层级模块：8 个实体
- 基础能力与构建平台：19 个实体
- 三方依赖与外部库：36 个实体
- 资源：14 个实体
- 清单与权限：6 个实体
- 构建与测试入口：5 个实体

## 实体补充任务

### 项目与模块

- [x] 补充实体：CapnoEasy（定位表 L24，领域：项目）：ID / 别名：rootProject, CapnoGraph；源文件：`settings.gradle.kts`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：Android 二氧化碳监测应用。
- [x] 补充实体：app module（定位表 L25，领域：模块）：ID / 别名：`:app`, `com.wldmedical.capnoeasy`, 主应用模块；源文件：`app/build.gradle.kts`, `app/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：主应用模块。
- [x] 补充实体：hotmeltprint module（定位表 L26，领域：模块）：ID / 别名：`:hotmeltprint`, `com.wldmedical.hotmeltprint`, 热敏打印模块；源文件：`hotmeltprint/build.gradle.kts`, `hotmeltprint/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：热敏打印 SDK 库模块。
- [x] 补充实体：Gradle settings（定位表 L27，领域：构建）：ID / 别名：repositories, included modules, Gradle 设置；源文件：`settings.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：根工程模块拓扑和仓库配置。
- [x] 补充实体：Version catalog（定位表 L28，领域：构建）：ID / 别名：libraries, plugins, 版本目录；源文件：`gradle/libs.versions.toml`；当前补充上下文：`context/docs/build-platform.md`；现有备注：依赖和插件版本。
- [x] 补充实体：Root build script（定位表 L29，领域：构建）：ID / 别名：project plugins, 根构建脚本；源文件：`build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：根工程 Gradle Kotlin DSL 配置。
### 源码包与层级模块

- [x] 补充实体：`com.wldmedical.capnoeasy` package（定位表 L35，领域：模块）：ID / 别名：app package, 主包；源文件：`app/src/main/java/com/wldmedical/capnoeasy/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：主应用 Kotlin 源码根包。
- [x] 补充实体：`pages` package（定位表 L36，领域：模块）：ID / 别名：Activity pages, 页面层；源文件：`app/src/main/java/com/wldmedical/capnoeasy/pages/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：页面 Activity 和页面级工作流。
- [x] 补充实体：`components` package（定位表 L37，领域：模块）：ID / 别名：Compose components, 组件层；源文件：`app/src/main/java/com/wldmedical/capnoeasy/components/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：可复用 Compose 组件和组件状态模型。
- [x] 补充实体：`kits` package（定位表 L38，领域：模块）：ID / 别名：service kits, 工具/服务层；源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：蓝牙、协议、存储、PDF、音频、图片和备份能力。
- [x] 补充实体：`kits.dbmigration` package（定位表 L39，领域：模块）：ID / 别名：Room migrations, 数据库迁移；源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/`；当前补充上下文：`app/data_version_list.txt`；现有备注：Room 版本迁移脚本目录。
- [x] 补充实体：`models` package（定位表 L40，领域：模块）：ID / 别名：app state, 状态层；源文件：`app/src/main/java/com/wldmedical/capnoeasy/models/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：全局状态容器和唯一 ViewModel。
- [x] 补充实体：`ui.theme` package（定位表 L41，领域：模块）：ID / 别名：Compose theme, 主题层；源文件：`app/src/main/java/com/wldmedical/capnoeasy/ui/theme/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：Compose Material3 主题、颜色和字体定义。
- [x] 补充实体：`com.wldmedical.hotmeltprint` package（定位表 L42，领域：模块）：ID / 别名：printer SDK wrapper, 热敏打印包；源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：GPrinter SDK 的项目内封装。
### 基础能力与构建平台

- [x] 补充实体：Gradle wrapper（定位表 L48，领域：构建平台）：ID / 别名：`./gradlew`, `gradle-8.10.2-all`, Gradle 8.10.2；源文件：`gradlew`, `gradle/wrapper/gradle-wrapper.properties`；当前补充上下文：`context/docs/build-platform.md`；现有备注：本地构建和测试入口；wrapper 使用华为云 Gradle 8.10.2 all 包。
- [x] 补充实体：Gradle Kotlin DSL（定位表 L49，领域：构建平台）：ID / 别名：`.gradle.kts`, `settings.gradle.kts`, Kotlin DSL；源文件：`settings.gradle.kts`, `build.gradle.kts`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：根工程和模块构建脚本均使用 Kotlin DSL。
- [x] 补充实体：Gradle version catalog（定位表 L50，领域：构建平台）：ID / 别名：`libs.versions.toml`, `libs.*`, `libs.plugins.*`, 版本目录；源文件：`gradle/libs.versions.toml`；当前补充上下文：`context/docs/build-platform.md`；现有备注：插件和多数依赖的集中别名与版本来源。
- [x] 补充实体：Maven repositories（定位表 L51，领域：构建平台）：ID / 别名：`google`, `mavenCentral`, `gradlePluginPortal`, `jitpack.io`, Huawei repo, Aliyun mirrors；源文件：`settings.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：插件和依赖仓库配置。
- [x] 补充实体：Android Gradle Plugin（定位表 L52，领域：构建平台）：ID / 别名：`agp=8.8.0`, `com.android.application`, `com.android.library`, AGP；源文件：`gradle/libs.versions.toml`, `build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 与 hotmeltprint 的 Android 插件版本。
- [x] 补充实体：Android application plugin（定位表 L53，领域：构建平台）：ID / 别名：`libs.plugins.android.application`, `com.android.application`, `:app`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：主应用模块使用的 Android Application 插件。
- [x] 补充实体：Android library plugin（定位表 L54，领域：构建平台）：ID / 别名：`libs.plugins.android.library`, `com.android.library`, `:hotmeltprint`；源文件：`gradle/libs.versions.toml`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：热敏打印库模块使用的 Android Library 插件。
- [x] 补充实体：Kotlin Android plugin（定位表 L55，领域：构建平台）：ID / 别名：`kotlin=2.0.0`, `org.jetbrains.kotlin.android`, `libs.plugins.kotlin.android`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：两个 Android 模块的 Kotlin 插件。
- [x] 补充实体：Kotlin Compose plugin（定位表 L56，领域：构建平台）：ID / 别名：`org.jetbrains.kotlin.plugin.compose`, `libs.plugins.kotlin.compose`, Compose compiler；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块启用 Compose 编译插件。
- [x] 补充实体：Kotlin kapt（定位表 L57，领域：构建平台）：ID / 别名：`kotlin-kapt`, kapt, annotation processing；源文件：`app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Hilt 和 Room 注解处理使用 kapt。
- [x] 补充实体：Kotlin JVM plugin（定位表 L58，领域：构建平台）：ID / 别名：`org.jetbrains.kotlin.jvm`, `jetbrains-kotlin-jvm=2.0.0`；源文件：`gradle/libs.versions.toml`, `build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：根构建脚本声明但当前没有模块应用。
- [x] 补充实体：Android SDK levels（定位表 L59，领域：Android 平台）：ID / 别名：`compileSdk=35`, `targetSdk=35`, `app minSdk=30`, `hotmeltprint minSdk=24`；源文件：`app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：以模块 build 文件为准。
- [x] 补充实体：Java and Kotlin JVM target（定位表 L60，领域：构建平台）：ID / 别名：`JavaVersion.VERSION_11`, `jvmTarget=11`, `org.gradle.java.home`；源文件：`gradle.properties`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：模块目标字节码为 Java 11；Gradle 属性里配置了 JDK 17 路径。
- [x] 补充实体：AndroidX and Jetifier（定位表 L61，领域：Android 平台）：ID / 别名：`android.useAndroidX=true`, `android.enableJetifier=true`, `android.nonTransitiveRClass=true`；源文件：`gradle.properties`；当前补充上下文：`context/docs/build-platform.md`；现有备注：AndroidX、Jetifier 和非传递 R 类配置。
- [x] 补充实体：Jetpack Compose UI（定位表 L62，领域：UI 平台）：ID / 别名：`buildFeatures.compose=true`, `androidx.compose`, `composeBom=2024.04.01`, Material3；源文件：`app/build.gradle.kts`, `gradle/libs.versions.toml`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块的 Compose、BOM、Material3、Navigation Compose 基础 UI 能力。
- [x] 补充实体：Hilt dependency injection（定位表 L63，领域：依赖注入）：ID / 别名：`com.google.dagger.hilt.android`, `hilt-android=2.51.1`, `@HiltAndroidApp`, `@HiltViewModel`；源文件：`build.gradle.kts`, `app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`, `app/src/main/java/com/wldmedical/capnoeasy/models/AppStateModel.kt`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块应用 Hilt 插件并配置 Hilt compiler。
- [x] 补充实体：Room persistence（定位表 L64，领域：存储平台）：ID / 别名：`androidx.room`, `room-runtime=2.5.2`, `room-compiler=2.5.2`, `room-ktx=2.5.2`, `room.schemaLocation`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Room 运行时、compiler、KTX 和 schema 导出路径。
- [x] 补充实体：Android test stack（定位表 L65，领域：测试）：ID / 别名：`junit=4.13.2`, `androidx.test.ext:junit=1.2.1`, `espresso-core=3.6.1`, `ui-test-junit4`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：JVM、Android instrumentation 和 Compose UI 测试依赖。
- [x] 补充实体：Core AndroidX libraries（定位表 L66，领域：Android 平台）：ID / 别名：`core-ktx`, `appcompat`, `activity-compose`, `lifecycle-runtime-ktx`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：两个模块共享或 app 使用的 AndroidX 基础库。
### 三方依赖与外部库

- [x] 补充实体：AndroidX Core KTX（定位表 L439，领域：三方依赖）：ID / 别名：`androidx.core:core-ktx:1.15.0`, coreKtx；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Android Kotlin 扩展基础库。
- [x] 补充实体：AndroidX Lifecycle Runtime KTX（定位表 L440，领域：三方依赖）：ID / 别名：`androidx.lifecycle:lifecycle-runtime-ktx:2.8.7`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Lifecycle 协程/运行时基础。
- [x] 补充实体：AndroidX Activity Compose（定位表 L441，领域：三方依赖）：ID / 别名：`androidx.activity:activity-compose:1.9.3`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Activity 内 `setContent` 和 Compose 集成。
- [x] 补充实体：Jetpack Compose BOM（定位表 L442，领域：三方依赖）：ID / 别名：`androidx.compose:compose-bom:2024.04.01`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Compose UI、graphics、tooling、Material3、测试依赖版本平台。
- [x] 补充实体：Compose UI（定位表 L443，领域：三方依赖）：ID / 别名：`androidx.compose.ui:ui`, `ui-graphics`, `ui-tooling`, `ui-tooling-preview`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app Compose 基础 UI 与预览/调试工具。
- [x] 补充实体：Compose Material3（定位表 L444，领域：三方依赖）：ID / 别名：`androidx.compose.material3:material3`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：页面和组件使用的 Material3 控件。
- [x] 补充实体：Navigation Compose（定位表 L445，领域：三方依赖）：ID / 别名：`androidx.navigation:navigation-compose:2.8.5`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：已声明依赖；当前页面导航主要使用 Intent。
- [x] 补充实体：ConstraintLayout（定位表 L446，领域：三方依赖）：ID / 别名：`androidx.constraintlayout:constraintlayout-core:1.1.0`, `constraintlayout:2.2.0`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的 ConstraintLayout 依赖。
- [x] 补充实体：DataBinding BaseLibrary（定位表 L447，领域：三方依赖）：ID / 别名：`androidx.databinding:baseLibrary:3.2.0-alpha11`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的 AndroidX databinding baseLibrary。
- [x] 补充实体：AndroidX AppCompat（定位表 L448，领域：三方依赖）：ID / 别名：`androidx.appcompat:appcompat:1.7.0`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：语言切换使用 AppCompatDelegate；两个模块均声明。
- [x] 补充实体：Android Identity JVM（定位表 L449，领域：三方依赖）：ID / 别名：`com.android.identity:identity-jvm:202411.1`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的 identity JVM 库。
- [x] 补充实体：AndroidX Bluetooth（定位表 L450，领域：三方依赖）：ID / 别名：`androidx.bluetooth:bluetooth:1.0.0-alpha02`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明；当前 BlueToothKit 主要直接使用 Android framework Bluetooth API。
- [x] 补充实体：Compose Foundation Android（定位表 L451，领域：三方依赖）：ID / 别名：`foundation-layout-android:1.7.2`, `foundation-android:1.7.8`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 和 hotmeltprint 的 Compose foundation 相关依赖。
- [x] 补充实体：Material Components（定位表 L452，领域：三方依赖）：ID / 别名：`com.google.android.material:material:1.12.0`；源文件：`gradle/libs.versions.toml`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：hotmeltprint 模块声明的 Material Components 依赖。
- [x] 补充实体：Compose Wheel Picker（定位表 L453，领域：三方依赖）：ID / 别名：`com.github.zj565061763:compose-wheel-picker:1.0.0-rc02`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/components/WheelPicker.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：WheelPicker 组件使用 `FVerticalWheelPicker`。
- [x] 补充实体：AndroidX Core SplashScreen（定位表 L454，领域：三方依赖）：ID / 别名：`androidx.core:core-splashscreen:1.0.1`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/pages/SplashActivity.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：app 模块声明的系统 SplashScreen 兼容库。
- [x] 补充实体：Hilt Android（定位表 L455，领域：三方依赖）：ID / 别名：`com.google.dagger:hilt-android:2.51.1`, `hilt-android-compiler`；源文件：`build.gradle.kts`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Application、BaseActivity、ViewModel 和 Kit 构造注入。
- [x] 补充实体：Hilt Navigation Compose（定位表 L456，领域：三方依赖）：ID / 别名：`androidx.hilt:hilt-navigation-compose:1.1.0-alpha01`；源文件：`app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的 Hilt Compose 辅助库。
- [x] 补充实体：Kotlin Reflect（定位表 L457，领域：三方依赖）：ID / 别名：`org.jetbrains.kotlin:kotlin-reflect`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：`getValueByKey` 使用 Kotlin 反射读取属性。
- [x] 补充实体：Coil（定位表 L458，领域：三方依赖）：ID / 别名：`io.coil-kt:coil:2.4.0`, `coil-compose:2.4.0`；源文件：`app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的图片加载依赖。
- [x] 补充实体：Accompanist DrawablePainter（定位表 L459，领域：三方依赖）：ID / 别名：`com.google.accompanist:accompanist-drawablepainter:0.37.0`；源文件：`app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的 drawable painter 辅助库。
- [x] 补充实体：Accompanist Pager（定位表 L460，领域：三方依赖）：ID / 别名：`accompanist-pager:0.28.0`, `accompanist-pager-indicators:0.28.0`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：EtCo2LineChart 使用 HorizontalPager 和 PagerIndicator。
- [x] 补充实体：MPAndroidChart（定位表 L461，领域：三方依赖）：ID / 别名：`com.github.PhilJay:MPAndroidChart:v3.1.0`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：实时图表、历史图表、PDF 图表和打印 Bitmap 渲染。
- [x] 补充实体：Room Runtime（定位表 L462，领域：三方依赖）：ID / 别名：`androidx.room:room-runtime:2.5.2`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`；当前补充上下文：`app/data_version_list.txt`；现有备注：Room 数据库运行时。
- [x] 补充实体：Room Compiler（定位表 L463，领域：三方依赖）：ID / 别名：`androidx.room:room-compiler:2.5.2`, kapt；源文件：`app/build.gradle.kts`；当前补充上下文：`app/data_version_list.txt`；现有备注：Room 注解处理器。
- [x] 补充实体：Room KTX（定位表 L464，领域：三方依赖）：ID / 别名：`androidx.room:room-ktx:2.5.2`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`；当前补充上下文：`app/data_version_list.txt`；现有备注：Room 协程/Flow 支持。
- [x] 补充实体：Gson（定位表 L465，领域：三方依赖）：ID / 别名：`com.google.code.gson:gson:2.10.1`；源文件：`app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt`；当前补充上下文：`app/data_version_list.txt`；现有备注：CO2WavePointData 压缩前 JSON 序列化和迁移解析。
- [x] 补充实体：iTextPDF（定位表 L466，领域：三方依赖）：ID / 别名：`com.itextpdf:itextpdf:5.5.13.4`；源文件：`app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：PDF 报告生成。
- [x] 补充实体：AndroidPdfViewer（定位表 L467，领域：三方依赖）：ID / 别名：`com.github.barteksc:android-pdf-viewer:3.2.0-beta.1`；源文件：`app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明，排除 `com.android.support`。
- [x] 补充实体：AndroidX Collection（定位表 L468，领域：三方依赖）：ID / 别名：`androidx.collection:collection:1.2.0`；源文件：`app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app 模块声明的集合工具依赖。
- [x] 补充实体：Bugly CrashReport AAR（定位表 L469，领域：三方依赖）：ID / 别名：`crashreport-4.1.9.3.aar`, `com.tencent.bugly.crashreport.CrashReport`；源文件：`app/libs/crashreport-4.1.9.3.aar`, `app/build.gradle.kts`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：本地 AAR 崩溃上报。
- [x] 补充实体：GPrinter SDK JAR（定位表 L470，领域：三方依赖）：ID / 别名：`SDKLib.jar`, `com.gprinter.*`；源文件：`hotmeltprint/libs/SDKLib.jar`, `hotmeltprint/build.gradle.kts`, `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：热敏打印 SDK，提供 BluetoothPort、EscCommand、PrinterDevices 等。
- [x] 补充实体：JUnit 4（定位表 L471，领域：测试依赖）：ID / 别名：`junit:junit:4.13.2`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：JVM 单元测试依赖。
- [x] 补充实体：AndroidX Test JUnit（定位表 L472，领域：测试依赖）：ID / 别名：`androidx.test.ext:junit:1.2.1`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Android instrumentation JUnit 扩展。
- [x] 补充实体：Espresso Core（定位表 L473，领域：测试依赖）：ID / 别名：`androidx.test.espresso:espresso-core:3.6.1`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：Android UI/instrumentation 测试依赖。
- [x] 补充实体：Compose UI Test（定位表 L474，领域：测试依赖）：ID / 别名：`ui-test-junit4`, `ui-test-manifest`；源文件：`gradle/libs.versions.toml`, `app/build.gradle.kts`；当前补充上下文：`context/docs/build-platform.md`；现有备注：app Compose UI 测试和 debug manifest 依赖。
### 资源

- [x] 补充实体：Chinese strings（定位表 L480，领域：资源）：ID / 别名：`values/strings.xml`, 中文文案；源文件：`app/src/main/res/values/strings.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：默认字符串资源。
- [x] 补充实体：English strings（定位表 L481，领域：资源）：ID / 别名：`values-en/strings.xml`, 英文文案；源文件：`app/src/main/res/values-en/strings.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：英文本地化字符串。
- [x] 补充实体：locales_config（定位表 L482，领域：资源）：ID / 别名：app locales, 应用语言配置；源文件：`app/src/main/res/xml/locales_config.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：语言切换的 Locale 配置。
- [x] 补充实体：Theme resources（定位表 L483，领域：资源）：ID / 别名：themes, colors, 主题资源；源文件：`app/src/main/res/values/themes.xml`, `app/src/main/res/values/colors.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：XML 主题和颜色资源。
- [x] 补充实体：Compose theme（定位表 L484，领域：UI 主题）：ID / 别名：Theme.kt, Color.kt, Type.kt, Compose 主题；源文件：`app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Theme.kt`, `app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Color.kt`, `app/src/main/java/com/wldmedical/capnoeasy/ui/theme/Type.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：Compose Material 主题。
- [x] 补充实体：App logos（定位表 L485，领域：资源）：ID / 别名：`app_logo`, `app_logo_round`, `wld_logo`, 应用 logo；源文件：`app/src/main/res/drawable/app_logo.webp`, `app/src/main/res/drawable/app_logo_round.webp`, `app/src/main/res/drawable/wld_logo.png`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：品牌资源。
- [x] 补充实体：Launcher icons（定位表 L486，领域：资源）：ID / 别名：`ic_launcher`, `ic_launcher_round`, foreground/background, 启动图标；源文件：`app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`, `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`, `app/src/main/res/drawable/ic_launcher_foreground.xml`, `app/src/main/res/drawable/ic_launcher_background.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：启动器图标资源。
- [x] 补充实体：Print icons（定位表 L487，领域：资源）：ID / 别名：`nav_print_btn`, `nav_print_stop_btn`, `print_pdf`, `print_ticket`, 打印图标；源文件：`app/src/main/res/drawable/nav_print_btn.png`, `app/src/main/res/drawable/nav_print_stop_btn.png`, `app/src/main/res/drawable/print_pdf.png`, `app/src/main/res/drawable/print_ticket.png`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：打印和 PDF UI 图片。
- [x] 补充实体：Range slider assets（定位表 L488，领域：资源）：ID / 别名：`both_range_left_thumb`, `both_range_right_thumb`, `oneside_range_thumb`, 滑块资源；源文件：`app/src/main/res/drawable/both_range_left_thumb.png`, `app/src/main/res/drawable/both_range_right_thumb.png`, `app/src/main/res/drawable/oneside_range_thumb.png`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：范围选择器滑块资源。
- [x] 补充实体：Device/UI icons（定位表 L489，领域：资源）：ID / 别名：`device_tyoe_mark`, `empty_device_list`, `pull_up`, `m3_*`, `fail_icon`, 设备和通用 UI 图标；源文件：`app/src/main/res/drawable/device_tyoe_mark.png`, `app/src/main/res/drawable/empty_device_list.png`, `app/src/main/res/drawable/pull_up.png`, `app/src/main/res/drawable/m3_arrow_forward.png`, `app/src/main/res/drawable/m3_lightbulb.png`, `app/src/main/res/drawable/m3_power_settings.png`, `app/src/main/res/drawable/m3_refresh.png`, `app/src/main/res/drawable/fail_icon.png`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：通用 UI 图片资源。
- [x] 补充实体：Alert sounds（定位表 L490，领域：音频）：ID / 别名：`low_level_alert`, `middle_level_alert`, 报警音；源文件：`app/src/main/res/raw/low_level_alert.wav`, `app/src/main/res/raw/middle_level_alert.wav`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：报警音频资源。
- [x] 补充实体：SimSun font（定位表 L491，领域：资源）：ID / 别名：`SimSun.ttf`, 宋体字体；源文件：`app/src/main/assets/fonts/SimSun.ttf`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：App/报告使用的字体资源。
- [x] 补充实体：Crashreport AAR（定位表 L492，领域：依赖）：ID / 别名：Bugly crashreport；源文件：`app/libs/crashreport-4.1.9.3.aar`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：内置崩溃上报 AAR。
- [x] 补充实体：Hotmelt SDK JAR（定位表 L493，领域：依赖）：ID / 别名：`SDKLib.jar`, 热敏打印 SDK；源文件：`hotmeltprint/libs/SDKLib.jar`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：内置热敏打印 SDK JAR。
### 清单与权限

- [x] 补充实体：App manifest（定位表 L499，领域：清单）：ID / 别名：permissions, activities, 应用清单；源文件：`app/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：主应用清单。
- [x] 补充实体：Hotmelt manifest（定位表 L500，领域：清单）：ID / 别名：printer library manifest, 打印库清单；源文件：`hotmeltprint/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：打印库清单。
- [x] 补充实体：Bluetooth permissions（定位表 L501，领域：清单）：ID / 别名：BLUETOOTH, BLUETOOTH_CONNECT, BLUETOOTH_SCAN, BLUETOOTH_ADVERTISE, 蓝牙权限；源文件：`app/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：蓝牙权限声明。
- [x] 补充实体：Location permissions（定位表 L502，领域：清单）：ID / 别名：ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION, 定位权限；源文件：`app/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：部分 Android 版本蓝牙扫描需要的定位权限。
- [x] 补充实体：Storage permissions（定位表 L503，领域：清单）：ID / 别名：MANAGE_EXTERNAL_STORAGE, READ/WRITE_EXTERNAL_STORAGE, 存储权限；源文件：`app/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：存储、导出和备份权限。
- [x] 补充实体：Network permissions（定位表 L504，领域：清单）：ID / 别名：INTERNET, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE, 网络权限；源文件：`app/src/main/AndroidManifest.xml`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：网络权限声明。
### 构建与测试入口

- [x] 补充实体：Gradle wrapper（定位表 L510，领域：构建）：ID / 别名：`./gradlew`, `gradle-8.10.2-all`, Gradle 包装器；源文件：`gradlew`, `gradle/wrapper/gradle-wrapper.properties`；当前补充上下文：`context/docs/build-platform.md`；现有备注：本地构建/测试入口。
- [x] 补充实体：app unit test（定位表 L511，领域：测试）：ID / 别名：ExampleUnitTest, app 单元测试；源文件：`app/src/test/java/com/wldmedical/capnoeasy/ExampleUnitTest.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：app JVM 测试占位文件。
- [x] 补充实体：app instrumented test（定位表 L512，领域：测试）：ID / 别名：ExampleInstrumentedTest, app 仪器测试；源文件：`app/src/androidTest/java/com/wldmedical/capnoeasy/ExampleInstrumentedTest.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：app Android 测试占位文件。
- [x] 补充实体：hotmeltprint unit test（定位表 L513，领域：测试）：ID / 别名：ExampleUnitTest, hotmeltprint 单元测试；源文件：`hotmeltprint/src/test/java/com/wldmedical/hotmeltprint/ExampleUnitTest.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：hotmeltprint JVM 测试占位文件。
- [x] 补充实体：hotmeltprint instrumented test（定位表 L514，领域：测试）：ID / 别名：ExampleInstrumentedTest, hotmeltprint 仪器测试；源文件：`hotmeltprint/src/androidTest/java/com/wldmedical/hotmeltprint/ExampleInstrumentedTest.kt`；当前补充上下文：`.cursor/rules/project-memory.mdc`；现有备注：hotmeltprint Android 测试占位文件。
