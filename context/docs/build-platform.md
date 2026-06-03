# 构建平台与基础能力

这个补充文档记录 CapnoEasy 当前使用的基础构建、语言、Android 平台和框架能力。若本文件与较旧的项目记忆不一致，以这里列出的源文件为当前事实来源，最终仍以代码和构建配置为准。

本批平台/依赖实体的单实体上下文文件位于 `context/docs/platform-resources/`，本文件保留构建平台总览和交叉核对表。

## 源文件

| 文件 | 用途 |
| --- | --- |
| `settings.gradle.kts` | 仓库配置和 Gradle 模块包含关系。 |
| `build.gradle.kts` | 根工程插件声明。 |
| `gradle/libs.versions.toml` | 插件与依赖版本目录。 |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle wrapper 发行包版本和下载地址。 |
| `gradle.properties` | Gradle、AndroidX、Jetifier、R8、Java home 等全局设置。 |
| `app/build.gradle.kts` | 主 Android 应用模块构建配置。 |
| `hotmeltprint/build.gradle.kts` | 热敏打印 Android Library 模块构建配置。 |

## 构建工具链 ID

| 能力 | ID / 坐标 | 版本 / 值 | 应用位置 | 备注 |
| --- | --- | --- | --- | --- |
| Gradle wrapper | `./gradlew`, `gradle-8.10.2-all` | 8.10.2 | 根工程 | wrapper URL 为 `https://repo.huaweicloud.com/gradle/gradle-8.10.2-all.zip`。 |
| Gradle Kotlin DSL | `.gradle.kts` | n/a | 根工程、`:app`、`:hotmeltprint` | 构建脚本均使用 Kotlin DSL。 |
| Version catalog | `gradle/libs.versions.toml`, `libs.*` | n/a | 根工程、模块 | 插件和多数依赖的集中别名与版本来源。 |
| Android Gradle Plugin | `com.android.application`, `com.android.library`, `agp` | 8.8.0 | `:app`、`:hotmeltprint` | 插件别名定义在版本目录。 |
| Kotlin Android plugin | `org.jetbrains.kotlin.android`, `kotlin` | 2.0.0 | `:app`、`:hotmeltprint` | 通过 `libs.plugins.kotlin.android` 应用。 |
| Kotlin Compose plugin | `org.jetbrains.kotlin.plugin.compose` | 2.0.0 | `:app` | app 模块开启 Compose 并应用 Compose 编译插件。 |
| Kotlin kapt | `kotlin-kapt` | 随 Kotlin 插件 | `:app` | 用于 Hilt 和 Room compiler。 |
| Kotlin JVM plugin | `org.jetbrains.kotlin.jvm`, `jetbrains-kotlin-jvm` | 2.0.0 | 仅声明 | 根工程 `apply false` 声明，当前没有模块应用。 |
| Hilt Gradle plugin | `com.google.dagger.hilt.android` | 2.51.1 | `:app` | 根工程声明插件，app 模块直接应用。 |

## Android 平台 ID

| 能力 | ID / 设置 | 值 | 来源 |
| --- | --- | --- | --- |
| app namespace | `namespace` | `com.wldmedical.capnoeasy` | `app/build.gradle.kts` |
| app application ID | `applicationId` | `com.wldmedical.capnoeasy` | `app/build.gradle.kts` |
| hotmeltprint namespace | `namespace` | `com.wldmedical.hotmeltprint` | `hotmeltprint/build.gradle.kts` |
| Compile SDK | `compileSdk` | 35 | 两个 Android 模块 |
| app minimum SDK | `minSdk` | 30 | `app/build.gradle.kts` |
| hotmeltprint minimum SDK | `minSdk` | 24 | `hotmeltprint/build.gradle.kts` |
| Target SDK | `targetSdk` | 35 | `app/build.gradle.kts` |
| app version code | `versionCode` | 3 | `app/build.gradle.kts` |
| app version name | `versionName` | `1.2` | `app/build.gradle.kts`, `app/src/main/AndroidManifest.xml` |
| Test runner | `testInstrumentationRunner` | `androidx.test.runner.AndroidJUnitRunner` | 两个 Android 模块 |

备注：`.cursor/rules/project-memory.mdc` 目前写 app 最低 SDK 是 33，但构建文件里的事实是 `app minSdk = 30`，`hotmeltprint minSdk = 24`。

## JVM 与 AndroidX 设置

| 能力 | ID / 设置 | 值 | 来源 |
| --- | --- | --- | --- |
| Java source compatibility | `sourceCompatibility` | `JavaVersion.VERSION_11` | 两个 Android 模块 |
| Java target compatibility | `targetCompatibility` | `JavaVersion.VERSION_11` | 两个 Android 模块 |
| Kotlin JVM target | `jvmTarget` | `11` | 两个 Android 模块 |
| Gradle Java home | `org.gradle.java.home` | `C:\\Program Files\\Java\\jdk-17` | `gradle.properties` |
| AndroidX | `android.useAndroidX` | `true` | `gradle.properties` |
| Jetifier | `android.enableJetifier` | `true` | `gradle.properties` |
| Non-transitive R | `android.nonTransitiveRClass` | `true` | `gradle.properties` |
| R8 | `android.enableR8` | `true` | `gradle.properties` |
| ZipAlign | `android.zipAlign` | `true` | `gradle.properties` |

备注：当前 `org.gradle.java.home` 是 Windows 路径；在 macOS/Linux 上执行 wrapper 时需要用 `-Dorg.gradle.java.home=<local JDK home>` 覆盖。本机依赖安装验证使用 `/opt/homebrew/Cellar/openjdk@21/21.0.10/libexec/openjdk.jdk/Contents/Home`，`./gradlew :app:assembleDebug` 可成功解析依赖并完成 Debug 构建。

## UI 与应用框架 ID

| 能力 | ID / 坐标 | 版本 / 值 | 来源 |
| --- | --- | --- | --- |
| Jetpack Compose feature | `buildFeatures.compose` | `true` | `app/build.gradle.kts` |
| Compose BOM | `androidx.compose:compose-bom`, `composeBom` | 2024.04.01 | `gradle/libs.versions.toml`, `app/build.gradle.kts` |
| Compose UI | `androidx.compose.ui:ui` | BOM 管理 | `gradle/libs.versions.toml` |
| Compose Material3 | `androidx.compose.material3:material3` | BOM 管理 | `gradle/libs.versions.toml` |
| Navigation Compose | `androidx.navigation:navigation-compose` | 2.8.5 | `gradle/libs.versions.toml` |
| Activity Compose | `androidx.activity:activity-compose` | 1.9.3 | `gradle/libs.versions.toml` |
| Compose Foundation | `androidx.compose.foundation:foundation-android` | 1.7.8 | `gradle/libs.versions.toml`, `hotmeltprint/build.gradle.kts` |
| Compose Foundation Layout | `androidx.compose.foundation:foundation-layout-android` | 1.7.2 | `gradle/libs.versions.toml`, `app/build.gradle.kts` |
| AppCompat | `androidx.appcompat:appcompat` | 1.7.0 | `gradle/libs.versions.toml` |
| Android Material Components | `com.google.android.material:material` | 1.12.0 | `gradle/libs.versions.toml` |

## 数据、注入与注解处理 ID

| 能力 | ID / 坐标 | 版本 / 值 | 来源 |
| --- | --- | --- | --- |
| Hilt Android runtime | `com.google.dagger:hilt-android` | 2.51.1 | `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts` |
| Hilt compiler | `com.google.dagger:hilt-android-compiler` | 2.51.1 | `app/build.gradle.kts` |
| Hilt Navigation Compose | `androidx.hilt:hilt-navigation-compose` | 1.1.0-alpha01 | `app/build.gradle.kts` |
| Room runtime | `androidx.room:room-runtime` | 2.5.2 | `app/build.gradle.kts` |
| Room compiler | `androidx.room:room-compiler` | 2.5.2 | `app/build.gradle.kts` |
| Room KTX | `androidx.room:room-ktx` | 2.5.2 | `app/build.gradle.kts` |
| Room schema export | `room.schemaLocation` | `$projectDir/schemas` | `app/build.gradle.kts` |
| Gson | `com.google.code.gson:gson` | 2.10.1 | `app/build.gradle.kts` |

## 设备、媒体与报告相关基础库 ID

| 能力 | ID / 坐标 | 版本 / 值 | 来源 |
| --- | --- | --- | --- |
| AndroidX Bluetooth | `androidx.bluetooth:bluetooth` | 1.0.0-alpha02 | `gradle/libs.versions.toml`, `app/build.gradle.kts` |
| MPAndroidChart | `com.github.PhilJay:MPAndroidChart` | v3.1.0 | `gradle/libs.versions.toml`, 两个模块 build 文件 |
| Coil | `io.coil-kt:coil`, `io.coil-kt:coil-compose` | 2.4.0 | `app/build.gradle.kts` |
| iTextPDF | `com.itextpdf:itextpdf` | 5.5.13.4 | 两个模块 build 文件 |
| AndroidPdfViewer | `com.github.barteksc:android-pdf-viewer` | 3.2.0-beta.1 | `app/build.gradle.kts` |
| Bugly crashreport | `app/libs/crashreport-4.1.9.3.aar` | 4.1.9.3 | `app/build.gradle.kts`, `app/libs/` |
| Hotmelt SDK JAR | `hotmeltprint/libs/SDKLib.jar` | 本地 JAR | `hotmeltprint/build.gradle.kts`, `hotmeltprint/libs/` |

## 仓库 ID

| 范围 | 仓库 ID / URL | 来源 |
| --- | --- | --- |
| Plugin management | `google`, `mavenCentral`, `gradlePluginPortal` | `settings.gradle.kts` |
| Dependency resolution | `google`, `mavenCentral` | `settings.gradle.kts` |
| Dependency resolution | `https://jitpack.io` | `settings.gradle.kts` |
| Dependency resolution | `https://developer.huawei.com/repo/` | `settings.gradle.kts` |
| Dependency resolution | `https://maven.aliyun.com/repository/google` | `settings.gradle.kts` |
| Dependency resolution | `https://maven.aliyun.com/repository/central` | `settings.gradle.kts` |
| Dependency resolution | `https://maven.aliyun.com/repository/jcenter` | `settings.gradle.kts` |

## 测试能力 ID

| 能力 | ID / 坐标 | 版本 / 值 | 来源 |
| --- | --- | --- | --- |
| JUnit 4 | `junit:junit` | 4.13.2 | `gradle/libs.versions.toml` |
| AndroidX JUnit | `androidx.test.ext:junit` | 1.2.1 | `gradle/libs.versions.toml` |
| Espresso Core | `androidx.test.espresso:espresso-core` | 3.6.1 | `gradle/libs.versions.toml` |
| Compose UI test | `androidx.compose.ui:ui-test-junit4`, `ui-test-manifest` | BOM 管理 | `gradle/libs.versions.toml`, `app/build.gradle.kts` |

## 三方依赖实体核对

本节按 `temp-task-list-1-platform-resources.md` 的三方依赖实体逐项补充责任、调用点、注意事项和最小验证。坐标以 `gradle/libs.versions.toml`、`app/build.gradle.kts`、`hotmeltprint/build.gradle.kts` 为准。

### AndroidX、Compose 与 UI 依赖

| 实体 | 坐标 / 版本 | 应用位置 | 调用点 / 责任 | 注意事项与最小验证 |
| --- | --- | --- | --- | --- |
| AndroidX Core KTX | `androidx.core:core-ktx:1.15.0` | `:app`, `:hotmeltprint` | Kotlin Android 基础扩展。 | 两个模块编译验证。 |
| AndroidX Lifecycle Runtime KTX | `androidx.lifecycle:lifecycle-runtime-ktx:2.8.7` | `:app` | Activity/Compose 生命周期协程基础。 | `:app:assembleDebug`。 |
| AndroidX Activity Compose | `androidx.activity:activity-compose:1.9.3` | `:app` | Activity `setContent` 接入 Compose。 | `SplashActivity` 和各 BaseActivity 页面编译验证。 |
| Jetpack Compose BOM | `androidx.compose:compose-bom:2024.04.01` | `:app` | 统一 Compose UI、graphics、tooling、Material3、测试依赖版本。 | 新增 Compose 依赖优先走 BOM；`dependencies` 任务可核对版本解析。 |
| Compose UI | `androidx.compose.ui:ui`, `ui-graphics`, `ui-tooling`, `ui-tooling-preview` | `:app` | Compose 页面、组件、预览和 debug tooling。 | `:app:assembleDebug`；debug 包含 tooling。 |
| Compose Material3 | `androidx.compose.material3:material3` | `:app` | 页面和组件使用 Material3 主题/控件。 | `CapnoEasyTheme` 编译和页面渲染验证。 |
| Navigation Compose | `androidx.navigation:navigation-compose:2.8.5` | `:app` | 依赖已声明。 | 当前页面导航主要使用 Intent；若引入 NavHost，需补页面路由文档和 UI 测试。 |
| ConstraintLayout | `androidx.constraintlayout:constraintlayout-core:1.1.0`, `constraintlayout:2.2.0` | `:app` | 依赖已声明。 | 当前 Compose 页面未检出直接调用；移除前先全局 `rg "Constraint"`。 |
| DataBinding BaseLibrary | `androidx.databinding:baseLibrary:3.2.0-alpha11` | `:app` | 依赖已声明。 | buildFeatures 未启用 dataBinding；移除/升级前检查历史兼容需求。 |
| AndroidX AppCompat | `androidx.appcompat:appcompat:1.7.0` | `:app`, `:hotmeltprint` | `AppStateModel` 使用 `AppCompatDelegate.setApplicationLocales` 切换语言。 | 语言切换需真机/模拟器验证；两个模块编译。 |
| Android Identity JVM | `com.android.identity:identity-jvm:202411.1` | `:app` | 依赖已声明。 | 当前源码未检出直接调用；移除前全局 `rg "identity"`。 |
| AndroidX Bluetooth | `androidx.bluetooth:bluetooth:1.0.0-alpha02` | `:app` | 依赖已声明。 | 当前 `BlueToothKit` 主要直接使用 Android framework Bluetooth API；BLE 变更需真机验证。 |
| Compose Foundation Android | `foundation-layout-android:1.7.2`, `foundation-android:1.7.8` | `:app`, `:hotmeltprint` | Compose foundation/layout 基础。 | `app/build.gradle.kts` 中 `foundation-layout-android` 重复声明 4 次；`hotmeltprint` 使用 foundation-android。 |
| Material Components | `com.google.android.material:material:1.12.0` | `:hotmeltprint` | Android View Material 组件基础。 | 当前打印库源码未检出直接调用；`hotmeltprint` 编译验证。 |
| Compose Wheel Picker | `com.github.zj565061763:compose-wheel-picker:1.0.0-rc02` | `:app` | `WheelPicker.kt` 使用 `FVerticalWheelPicker` 和 `rememberFWheelPickerState`。 | 改版本后验证设置页滚轮选择和 `wheelPickerConfig` 默认索引。 |
| AndroidX Core SplashScreen | `androidx.core:core-splashscreen:1.0.1` | `:app` | 依赖已声明；Splash 页面当前自绘 Compose 动画。 | `SplashActivity` 未检出 `installSplashScreen` 调用；若改用系统 Splash API 需补启动页行为文档。 |
| Kotlin Reflect | `org.jetbrains.kotlin:kotlin-reflect` | `:app` | `CapnoEasyConstant.getValueByKey` 通过反射读取对象属性。 | 常量反射逻辑变更需单测或手动验证 key 到值映射。 |
| Coil | `io.coil-kt:coil:2.4.0`, `coil-compose:2.4.0` | `:app` | 依赖已声明。 | 当前源码未检出直接调用；新增远程/本地图片加载时再补调用点。 |
| Accompanist DrawablePainter | `accompanist-drawablepainter:0.37.0` | `:app` | 依赖已声明。 | 当前源码未检出直接调用；移除前检查 drawable painter 使用。 |
| Accompanist Pager | `accompanist-pager:0.28.0`, `accompanist-pager-indicators:0.28.0` | `:app` | `EtCo2LineChart` 使用 `HorizontalPager` 和 `HorizontalPagerIndicator`。 | 改版本后验证实时波形页翻页和 indicator。 |

### 注入、数据、报告、设备与本地 SDK 依赖

| 实体 | 坐标 / 版本 | 应用位置 | 调用点 / 责任 | 注意事项与最小验证 |
| --- | --- | --- | --- | --- |
| Hilt Android | `com.google.dagger:hilt-android:2.51.1`, `hilt-android-compiler` | `:app`, `:hotmeltprint` | `CapnoEasyApplication` 使用 `@HiltAndroidApp`；`AppStateModel`/Activity/Kit 构造注入依赖 compiler。 | `:app:kaptDebugKotlin` 或 `:app:assembleDebug` 验证注解处理。 |
| Hilt Navigation Compose | `androidx.hilt:hilt-navigation-compose:1.1.0-alpha01` | `:app` | 依赖已声明。 | 当前未检出 `hiltViewModel` 直接调用；若引入 NavHost ViewModel 作用域需补文档。 |
| MPAndroidChart | `com.github.PhilJay:MPAndroidChart:v3.1.0` | `:app`, `:hotmeltprint` | `EtCo2LineChart`, `HistoryRecordDetailActivity`, `PDFKit`, `HotmeltPinter` 使用 `LineChart` 渲染实时/历史/PDF/打印图表。 | `hotmeltprint/build.gradle.kts` 重复声明 3 次；图表变更需页面、PDF、打印 bitmap 均验证。 |
| Room Runtime | `androidx.room:room-runtime:2.5.2` | `:app` | `LocalStorageKit` 定义 Room database/entity/dao 运行时。 | schema 输出到 `app/schemas`；数据库变更需迁移和 schema 验证。 |
| Room Compiler | `androidx.room:room-compiler:2.5.2` | `:app` kapt | Room 注解处理器。 | `kapt` 配置 `room.schemaLocation=$projectDir/schemas`；运行 `:app:kaptDebugKotlin`。 |
| Room KTX | `androidx.room:room-ktx:2.5.2` | `:app` | Room 协程/Flow 支持。 | 数据访问变更需结合 `LocalStorageKit` 验证。 |
| Gson | `com.google.code.gson:gson:2.10.1` | `:app` | `LocalStorageKit` 序列化/反序列化 Patient、CO2WavePointData；迁移脚本解析旧数据。 | 数据模型字段变更需兼容历史 JSON。 |
| iTextPDF | `com.itextpdf:itextpdf:5.5.13.4` | `:app`, `:hotmeltprint` | `PDFKit` 生成 PDF 报告，打印模块也声明。 | 验证中文字体、图表图片和 SAF 导出；库版本较旧，升级需回归 PDF 格式。 |
| AndroidPdfViewer | `com.github.barteksc:android-pdf-viewer:3.2.0-beta.1` | `:app` | 依赖已声明并排除 `com.android.support`。 | 当前源码未检出直接调用；如启用 PDF 预览需补 Activity/组件映射。 |
| AndroidX Collection | `androidx.collection:collection:1.2.0` | `:app` | 依赖已声明。 | 当前源码未检出直接调用；移除前全局检查。 |
| Bugly CrashReport AAR | `app/libs/crashreport-4.1.9.3.aar`, `com.tencent.bugly.crashreport.CrashReport` | `:app` | Application import CrashReport；Manifest 配置 Bugly app id/version/debug metadata。 | `CrashReport.initCrashReport` 当前被注释；重新启用需网络权限和崩溃上报验证。 |
| GPrinter SDK JAR | `hotmeltprint/libs/SDKLib.jar`, `com.gprinter.*` | `:hotmeltprint` | `HotmeltPinter` 使用 `BluetoothPort`, `PortManager`, `EscCommand`, `PrinterDevices` 打印。 | build 文件同时 fileTree 和 `files("libs/SDKLib.jar")` 引入；打印能力需真机和打印机验证。 |

### 测试依赖

| 实体 | 坐标 / 版本 | 应用位置 | 调用点 / 责任 | 注意事项与最小验证 |
| --- | --- | --- | --- | --- |
| JUnit 4 | `junit:junit:4.13.2` | 两个模块 test | `ExampleUnitTest` 使用 `assertEquals`。 | `./gradlew :app:testDebugUnitTest :hotmeltprint:testDebugUnitTest`。 |
| AndroidX Test JUnit | `androidx.test.ext:junit:1.2.1` | 两个模块 androidTest | `ExampleInstrumentedTest` instrumentation runner。 | 需连接设备执行 `connectedDebugAndroidTest`。 |
| Espresso Core | `androidx.test.espresso:espresso-core:3.6.1` | 两个模块 androidTest | Android UI/instrumentation 测试基础。 | 当前示例测试未直接使用 Espresso API；新增 UI 测试时补调用点。 |
| Compose UI Test | `ui-test-junit4`, `ui-test-manifest` | `:app` androidTest/debug | Compose UI 测试依赖和 debug manifest。 | 当前未检出 Compose UI 测试文件；新增测试时执行 `:app:connectedDebugAndroidTest`。 |

## 构建注意事项与最小验证

- `app/build.gradle.kts` 定义 `val vicoVersion = "2.0.1"`，当前未被依赖使用；`gradle/libs.versions.toml` 定义 `firebase-config-ktx`，当前也未被模块 build 文件引用。
- `foundation-layout-android` 在 app 依赖中重复声明 4 次，`mpandroidchart` 在 hotmeltprint 依赖中重复声明 3 次，`SDKLib.jar` 可能被 fileTree 和显式 files 重复引入。
- `.cursor/rules/project-memory.mdc` 中 app 最低 SDK 33 与构建文件不一致；当前事实是 app `minSdk=30`，hotmeltprint `minSdk=24`。
- 依赖坐标变更后，最低验证是 `./gradlew :app:assembleDebug :hotmeltprint:assembleDebug`；涉及注解处理还应执行 `./gradlew :app:kaptDebugKotlin`。
- 资源或 Manifest 变更后的上下文见 `context/docs/platform-resources.md`。
