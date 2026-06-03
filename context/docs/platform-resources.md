# 平台、资源、清单与测试入口

本文档是 `temp-task-list-1-platform-resources.md` 的总览/索引。实体级事实以 `context/docs/platform-resources/` 下的单个 Markdown 文件为准：一个实体对应一个上下文文档；`context/entity-id-mapping.md` 的本批实体 `补充上下文` 均应指向这些单实体文件。

## 源文件范围

| 文件 | 用途 |
| --- | --- |
| `settings.gradle.kts` | 根工程名、模块包含关系和仓库配置。 |
| `build.gradle.kts` | 根工程插件声明。 |
| `gradle/libs.versions.toml` | 插件与依赖版本目录。 |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle wrapper 发行包版本和下载地址。 |
| `gradle.properties` | Gradle、AndroidX、Jetifier、R8、Java home 等全局设置。 |
| `app/build.gradle.kts` | 主应用模块 ID、SDK、版本、资源打包和依赖入口。 |
| `hotmeltprint/build.gradle.kts` | 热敏打印库模块 ID、SDK、依赖入口。 |
| `app/src/main/AndroidManifest.xml` | app 权限、硬件特性、Application、Activity、Bugly metadata。 |
| `hotmeltprint/src/main/AndroidManifest.xml` | 打印库 Manifest，目前为空壳 manifest。 |
| `app/src/main/res/` | 字符串、主题、图标、图片、音频、xml 配置等 Android 资源。 |
| `app/src/main/assets/fonts/SimSun.ttf` | PDF/报告可用的宋体字体资源。 |
| `app/libs/crashreport-4.1.9.3.aar` | 本地 Bugly CrashReport AAR。 |
| `hotmeltprint/libs/SDKLib.jar` | GPrinter 热敏打印 SDK JAR。 |
| `app/src/test/`, `app/src/androidTest/` | app JVM 单元测试和 instrumentation 测试入口。 |
| `hotmeltprint/src/test/`, `hotmeltprint/src/androidTest/` | hotmeltprint JVM 单元测试和 instrumentation 测试入口。 |

## 当前事实摘要

- 根工程名为 `CapnoEasy`，包含 `:app` 和 `:hotmeltprint` 两个模块。
- app namespace/applicationId 为 `com.wldmedical.capnoeasy`，`compileSdk=35`、`minSdk=30`、`targetSdk=35`、`versionName=1.2`。
- hotmeltprint namespace 为 `com.wldmedical.hotmeltprint`，`compileSdk=35`、`minSdk=24`。
- Gradle wrapper 使用华为云 `gradle-8.10.2-all.zip`；构建脚本均为 Kotlin DSL；macOS/Linux 执行时需覆盖 `gradle.properties` 中的 Windows `org.gradle.java.home`。
- app Manifest 声明蓝牙、定位、存储和网络权限；SplashActivity 是 launcher，其他 Activity exported=false。
- `values/strings.xml` 是默认中文资源，`values-en/strings.xml` 是英文资源，`locales_config.xml` 声明 `en` 和 `zh`。
- Bugly AAR 和 GPrinter SDK JAR 是本地二进制依赖；CrashReport 初始化当前在 Application 中注释。

## 实体文档索引

| 序号 | 实体 | 领域 | 实体上下文 | 备注 |
| --- | --- | --- | --- | --- |
| 001 | CapnoEasy | 项目 | `context/docs/platform-resources/001-capnoeasy.md` | Android 二氧化碳监测应用 |
| 002 | app module | 模块 | `context/docs/platform-resources/002-app-module.md` | 主应用模块 |
| 003 | hotmeltprint module | 模块 | `context/docs/platform-resources/003-hotmeltprint-module.md` | 热敏打印 SDK 库模块 |
| 004 | Gradle settings | 构建 | `context/docs/platform-resources/004-gradle-settings.md` | 根工程模块拓扑和仓库配置 |
| 005 | Version catalog | 构建 | `context/docs/platform-resources/005-version-catalog.md` | 依赖和插件版本 |
| 006 | Root build script | 构建 | `context/docs/platform-resources/006-root-build-script.md` | 根工程 Gradle Kotlin DSL 配置 |
| 007 | `com.wldmedical.capnoeasy` package | 模块 | `context/docs/platform-resources/007-com-wldmedical-capnoeasy-package.md` | 主应用 Kotlin 源码根包 |
| 008 | `pages` package | 模块 | `context/docs/platform-resources/008-pages-package.md` | 页面 Activity 和页面级工作流 |
| 009 | `components` package | 模块 | `context/docs/platform-resources/009-components-package.md` | 可复用 Compose 组件和组件状态模型 |
| 010 | `kits` package | 模块 | `context/docs/platform-resources/010-kits-package.md` | 蓝牙、协议、存储、PDF、音频、图片和备份能力 |
| 011 | `kits.dbmigration` package | 模块 | `context/docs/platform-resources/011-kits-dbmigration-package.md` | Room 版本迁移脚本目录 |
| 012 | `models` package | 模块 | `context/docs/platform-resources/012-models-package.md` | 全局状态容器和唯一 ViewModel |
| 013 | `ui.theme` package | 模块 | `context/docs/platform-resources/013-ui-theme-package.md` | Compose Material3 主题、颜色和字体定义 |
| 014 | `com.wldmedical.hotmeltprint` package | 模块 | `context/docs/platform-resources/014-com-wldmedical-hotmeltprint-package.md` | GPrinter SDK 的项目内封装 |
| 015 | Gradle wrapper | 构建平台 | `context/docs/platform-resources/015-gradle-wrapper.md` | 本地构建和测试入口；wrapper 使用华为云 Gradle 8.10.2 all 包 |
| 016 | Gradle Kotlin DSL | 构建平台 | `context/docs/platform-resources/016-gradle-kotlin-dsl.md` | 根工程和模块构建脚本均使用 Kotlin DSL |
| 017 | Gradle version catalog | 构建平台 | `context/docs/platform-resources/017-gradle-version-catalog.md` | 插件和多数依赖的集中别名与版本来源 |
| 018 | Maven repositories | 构建平台 | `context/docs/platform-resources/018-maven-repositories.md` | 插件和依赖仓库配置 |
| 019 | Android Gradle Plugin | 构建平台 | `context/docs/platform-resources/019-android-gradle-plugin.md` | app 与 hotmeltprint 的 Android 插件版本 |
| 020 | Android application plugin | 构建平台 | `context/docs/platform-resources/020-android-application-plugin.md` | 主应用模块使用的 Android Application 插件 |
| 021 | Android library plugin | 构建平台 | `context/docs/platform-resources/021-android-library-plugin.md` | 热敏打印库模块使用的 Android Library 插件 |
| 022 | Kotlin Android plugin | 构建平台 | `context/docs/platform-resources/022-kotlin-android-plugin.md` | 两个 Android 模块的 Kotlin 插件 |
| 023 | Kotlin Compose plugin | 构建平台 | `context/docs/platform-resources/023-kotlin-compose-plugin.md` | app 模块启用 Compose 编译插件 |
| 024 | Kotlin kapt | 构建平台 | `context/docs/platform-resources/024-kotlin-kapt.md` | Hilt 和 Room 注解处理使用 kapt |
| 025 | Kotlin JVM plugin | 构建平台 | `context/docs/platform-resources/025-kotlin-jvm-plugin.md` | 根构建脚本声明但当前没有模块应用 |
| 026 | Android SDK levels | Android 平台 | `context/docs/platform-resources/026-android-sdk-levels.md` | 以模块 build 文件为准 |
| 027 | Java and Kotlin JVM target | 构建平台 | `context/docs/platform-resources/027-java-and-kotlin-jvm-target.md` | 模块目标字节码为 Java 11；Gradle 属性里配置了 JDK 17 路径 |
| 028 | AndroidX and Jetifier | Android 平台 | `context/docs/platform-resources/028-androidx-and-jetifier.md` | AndroidX、Jetifier 和非传递 R 类配置 |
| 029 | Jetpack Compose UI | UI 平台 | `context/docs/platform-resources/029-jetpack-compose-ui.md` | app 模块的 Compose、BOM、Material3、Navigation Compose 基础 UI 能力 |
| 030 | Hilt dependency injection | 依赖注入 | `context/docs/platform-resources/030-hilt-dependency-injection.md` | app 模块应用 Hilt 插件并配置 Hilt compiler |
| 031 | Room persistence | 存储平台 | `context/docs/platform-resources/031-room-persistence.md` | Room 运行时、compiler、KTX 和 schema 导出路径 |
| 032 | Android test stack | 测试 | `context/docs/platform-resources/032-android-test-stack.md` | JVM、Android instrumentation 和 Compose UI 测试依赖 |
| 033 | Core AndroidX libraries | Android 平台 | `context/docs/platform-resources/033-core-androidx-libraries.md` | 两个模块共享或 app 使用的 AndroidX 基础库 |
| 034 | AndroidX Core KTX | 三方依赖 | `context/docs/platform-resources/034-androidx-core-ktx.md` | Android Kotlin 扩展基础库 |
| 035 | AndroidX Lifecycle Runtime KTX | 三方依赖 | `context/docs/platform-resources/035-androidx-lifecycle-runtime-ktx.md` | Lifecycle 协程/运行时基础 |
| 036 | AndroidX Activity Compose | 三方依赖 | `context/docs/platform-resources/036-androidx-activity-compose.md` | Activity 内 `setContent` 和 Compose 集成 |
| 037 | Jetpack Compose BOM | 三方依赖 | `context/docs/platform-resources/037-jetpack-compose-bom.md` | Compose UI、graphics、tooling、Material3、测试依赖版本平台 |
| 038 | Compose UI | 三方依赖 | `context/docs/platform-resources/038-compose-ui.md` | app Compose 基础 UI 与预览/调试工具 |
| 039 | Compose Material3 | 三方依赖 | `context/docs/platform-resources/039-compose-material3.md` | 页面和组件使用的 Material3 控件 |
| 040 | Navigation Compose | 三方依赖 | `context/docs/platform-resources/040-navigation-compose.md` | 已声明依赖；当前页面导航主要使用 Intent |
| 041 | ConstraintLayout | 三方依赖 | `context/docs/platform-resources/041-constraintlayout.md` | app 模块声明的 ConstraintLayout 依赖 |
| 042 | DataBinding BaseLibrary | 三方依赖 | `context/docs/platform-resources/042-databinding-baselibrary.md` | app 模块声明的 AndroidX databinding baseLibrary |
| 043 | AndroidX AppCompat | 三方依赖 | `context/docs/platform-resources/043-androidx-appcompat.md` | 语言切换使用 AppCompatDelegate；两个模块均声明 |
| 044 | Android Identity JVM | 三方依赖 | `context/docs/platform-resources/044-android-identity-jvm.md` | app 模块声明的 identity JVM 库 |
| 045 | AndroidX Bluetooth | 三方依赖 | `context/docs/platform-resources/045-androidx-bluetooth.md` | app 模块声明；当前 BlueToothKit 主要直接使用 Android framework Bluetooth API |
| 046 | Compose Foundation Android | 三方依赖 | `context/docs/platform-resources/046-compose-foundation-android.md` | app 和 hotmeltprint 的 Compose foundation 相关依赖 |
| 047 | Material Components | 三方依赖 | `context/docs/platform-resources/047-material-components.md` | hotmeltprint 模块声明的 Material Components 依赖 |
| 048 | Compose Wheel Picker | 三方依赖 | `context/docs/platform-resources/048-compose-wheel-picker.md` | WheelPicker 组件使用 `FVerticalWheelPicker` |
| 049 | AndroidX Core SplashScreen | 三方依赖 | `context/docs/platform-resources/049-androidx-core-splashscreen.md` | app 模块声明的系统 SplashScreen 兼容库 |
| 050 | Hilt Android | 三方依赖 | `context/docs/platform-resources/050-hilt-android.md` | Application、BaseActivity、ViewModel 和 Kit 构造注入 |
| 051 | Hilt Navigation Compose | 三方依赖 | `context/docs/platform-resources/051-hilt-navigation-compose.md` | app 模块声明的 Hilt Compose 辅助库 |
| 052 | Kotlin Reflect | 三方依赖 | `context/docs/platform-resources/052-kotlin-reflect.md` | `getValueByKey` 使用 Kotlin 反射读取属性 |
| 053 | Coil | 三方依赖 | `context/docs/platform-resources/053-coil.md` | app 模块声明的图片加载依赖 |
| 054 | Accompanist DrawablePainter | 三方依赖 | `context/docs/platform-resources/054-accompanist-drawablepainter.md` | app 模块声明的 drawable painter 辅助库 |
| 055 | Accompanist Pager | 三方依赖 | `context/docs/platform-resources/055-accompanist-pager.md` | EtCo2LineChart 使用 HorizontalPager 和 PagerIndicator |
| 056 | MPAndroidChart | 三方依赖 | `context/docs/platform-resources/056-mpandroidchart.md` | 实时图表、历史图表、PDF 图表和打印 Bitmap 渲染 |
| 057 | Room Runtime | 三方依赖 | `context/docs/platform-resources/057-room-runtime.md` | Room 数据库运行时 |
| 058 | Room Compiler | 三方依赖 | `context/docs/platform-resources/058-room-compiler.md` | Room 注解处理器 |
| 059 | Room KTX | 三方依赖 | `context/docs/platform-resources/059-room-ktx.md` | Room 协程/Flow 支持 |
| 060 | Gson | 三方依赖 | `context/docs/platform-resources/060-gson.md` | CO2WavePointData 压缩前 JSON 序列化和迁移解析 |
| 061 | iTextPDF | 三方依赖 | `context/docs/platform-resources/061-itextpdf.md` | PDF 报告生成 |
| 062 | AndroidPdfViewer | 三方依赖 | `context/docs/platform-resources/062-androidpdfviewer.md` | app 模块声明，排除 `com.android.support` |
| 063 | AndroidX Collection | 三方依赖 | `context/docs/platform-resources/063-androidx-collection.md` | app 模块声明的集合工具依赖 |
| 064 | Bugly CrashReport AAR | 三方依赖 | `context/docs/platform-resources/064-bugly-crashreport-aar.md` | 本地 AAR 崩溃上报 |
| 065 | GPrinter SDK JAR | 三方依赖 | `context/docs/platform-resources/065-gprinter-sdk-jar.md` | 热敏打印 SDK，提供 BluetoothPort、EscCommand、PrinterDevices 等 |
| 066 | JUnit 4 | 测试依赖 | `context/docs/platform-resources/066-junit-4.md` | JVM 单元测试依赖 |
| 067 | AndroidX Test JUnit | 测试依赖 | `context/docs/platform-resources/067-androidx-test-junit.md` | Android instrumentation JUnit 扩展 |
| 068 | Espresso Core | 测试依赖 | `context/docs/platform-resources/068-espresso-core.md` | Android UI/instrumentation 测试依赖 |
| 069 | Compose UI Test | 测试依赖 | `context/docs/platform-resources/069-compose-ui-test.md` | app Compose UI 测试和 debug manifest 依赖 |
| 070 | Chinese strings | 资源 | `context/docs/platform-resources/070-chinese-strings.md` | 默认字符串资源 |
| 071 | English strings | 资源 | `context/docs/platform-resources/071-english-strings.md` | 英文本地化字符串 |
| 072 | locales_config | 资源 | `context/docs/platform-resources/072-locales-config.md` | 语言切换的 Locale 配置 |
| 073 | Theme resources | 资源 | `context/docs/platform-resources/073-theme-resources.md` | XML 主题和颜色资源 |
| 074 | Compose theme | UI 主题 | `context/docs/platform-resources/074-compose-theme.md` | Compose Material 主题 |
| 075 | App logos | 资源 | `context/docs/platform-resources/075-app-logos.md` | 品牌资源 |
| 076 | Launcher icons | 资源 | `context/docs/platform-resources/076-launcher-icons.md` | 启动器图标资源 |
| 077 | Print icons | 资源 | `context/docs/platform-resources/077-print-icons.md` | 打印和 PDF UI 图片 |
| 078 | Range slider assets | 资源 | `context/docs/platform-resources/078-range-slider-assets.md` | 范围选择器滑块资源 |
| 079 | Device/UI icons | 资源 | `context/docs/platform-resources/079-device-ui-icons.md` | 通用 UI 图片资源 |
| 080 | Alert sounds | 音频 | `context/docs/platform-resources/080-alert-sounds.md` | 报警音频资源 |
| 081 | SimSun font | 资源 | `context/docs/platform-resources/081-simsun-font.md` | App/报告使用的字体资源 |
| 082 | Crashreport AAR | 依赖 | `context/docs/platform-resources/082-crashreport-aar.md` | 内置崩溃上报 AAR |
| 083 | Hotmelt SDK JAR | 依赖 | `context/docs/platform-resources/083-hotmelt-sdk-jar.md` | 内置热敏打印 SDK JAR |
| 084 | App manifest | 清单 | `context/docs/platform-resources/084-app-manifest.md` | 主应用清单 |
| 085 | Hotmelt manifest | 清单 | `context/docs/platform-resources/085-hotmelt-manifest.md` | 打印库清单 |
| 086 | Bluetooth permissions | 清单 | `context/docs/platform-resources/086-bluetooth-permissions.md` | 蓝牙权限声明 |
| 087 | Location permissions | 清单 | `context/docs/platform-resources/087-location-permissions.md` | 部分 Android 版本蓝牙扫描需要的定位权限 |
| 088 | Storage permissions | 清单 | `context/docs/platform-resources/088-storage-permissions.md` | 存储、导出和备份权限 |
| 089 | Network permissions | 清单 | `context/docs/platform-resources/089-network-permissions.md` | 网络权限声明 |
| 090 | Gradle wrapper | 构建 | `context/docs/platform-resources/090-gradle-wrapper.md` | 本地构建/测试入口 |
| 091 | app unit test | 测试 | `context/docs/platform-resources/091-app-unit-test.md` | app JVM 测试占位文件 |
| 092 | app instrumented test | 测试 | `context/docs/platform-resources/092-app-instrumented-test.md` | app Android 测试占位文件 |
| 093 | hotmeltprint unit test | 测试 | `context/docs/platform-resources/093-hotmeltprint-unit-test.md` | hotmeltprint JVM 测试占位文件 |
| 094 | hotmeltprint instrumented test | 测试 | `context/docs/platform-resources/094-hotmeltprint-instrumented-test.md` | hotmeltprint Android 测试占位文件 |

## 最小验证策略

- 文档或映射变更：检查 `context/entity-id-mapping.md` 是否指向单实体文件，并确认 `context/docs/platform-resources/` 下有 94 个 Markdown 文件。
- 资源 key 变更：运行 `./gradlew :app:assembleDebug`，并用 `rg "R\.(string|drawable|raw|xml)\.<key>" app/src/main/java` 核对调用点。
- Manifest/权限变更：运行 `./gradlew :app:processDebugMainManifest` 或 `./gradlew :app:assembleDebug`，并在 Android 12+ 设备验证蓝牙运行时权限。
- 打印 SDK/JAR 变更：运行 `./gradlew :hotmeltprint:assembleDebug`，真机连接 GPrinter 设备验证 `HotmeltPinter.connect` 和 `print`。
- 测试入口变更：JVM 测试用 `:app:testDebugUnitTest` 和 `:hotmeltprint:testDebugUnitTest`；instrumentation 测试需要连接设备后执行 `connectedDebugAndroidTest`。
