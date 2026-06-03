# CapnoEasy 项目潜在问题分析

生成时间：2026-06-04

范围：当前工作区整体分析，仅新增本报告，未修改业务代码、构建脚本、资源文件或 context 文档。当前工作区已有未提交修改，结论按当前文件快照评估。

## 分析依据

- Context：`context/entity-id-mapping.md`、`context/docs/build-platform.md`、`context/docs/core-data-protocol/*`、`context/docs/app-ui-workflows/*`。
- 核心实体：`PDFKit` / `SaveChartToPdfTask`、`PdfReportTemplateConfig`、`PrintSetting`、`AppStateModel`、`BlueToothKit`、`LocalStorageKit` / `AppDatabase`、`HotmeltPinter`。
- 扫描范围：Android Gradle 配置、Manifest、Room 数据层、蓝牙链路、PDF 报告、打印模块、测试目录、生产日志和 TODO。

## 验证结果

- `./gradlew :app:compileDebugKotlin :hotmeltprint:compileDebugKotlin --offline --console=plain`：通过。
- `./gradlew :app:testDebugUnitTest :hotmeltprint:testDebugUnitTest --offline --console=plain`：通过。
- `./gradlew :app:lintDebug :hotmeltprint:lintDebug --offline --console=plain`：失败。离线缓存缺少部分 Compose / androidTest 依赖，例如 `androidx.compose.ui:ui-test-junit4:1.7.2`。
- `./gradlew :app:lintDebug :hotmeltprint:lintDebug --console=plain`：通过。
- 未覆盖真机安装、PDF 视觉结果、蓝牙实测和热敏打印实测。

## P0：当前无构建阻塞

当前快照已通过 Kotlin 编译、Debug 单元测试和非离线 lint，未发现阻塞提交的构建问题。本次涉及的 PDF/context 文档已按当前源码同步；仍需注意本地构建体验依赖缓存状态，离线 lint 在干净机器上可能失败。

## P1：高风险问题

3. Manifest 权限和发布合规风险偏高。
   - 证据：`app/src/main/AndroidManifest.xml:16-19` 声明 `ACCESS_BACKGROUND_LOCATION`、`MANAGE_EXTERNAL_STORAGE`、旧存储权限；`AndroidManifest.xml:29-30` 启用 `requestLegacyExternalStorage`、`allowBackup=true`；`AndroidManifest.xml:50` 启用 `BUGLY_ENABLE_DEBUG`。
   - 风险：上架审核、Android 11+ 存储策略、隐私说明和生产数据备份都有阻力；`allowBackup=true` 对医疗/患者数据尤其敏感。
   - 建议：按功能真实需要拆分权限，移除无用权限；若必须保留后台定位或全文件访问，补充产品合规说明、运行时解释和测试矩阵；生产包关闭 Bugly debug，并复核备份策略。

4. 蓝牙权限和生命周期依赖大量 suppress。
   - 证据：`BlueToothKit.kt` 多处 `@SuppressLint("MissingPermission")`，`SearchActivity.kt`、`DeviceList.kt`、`EtCo2LineChart.kt` 也 suppress 蓝牙权限检查；`MainActivity.kt:78/81`、`BlueToothKit.kt:717` 仍使用 `startActivityForResult`。
   - 风险：Android 12+ 蓝牙权限状态变化时，编译期检查被绕过，运行期更容易出现连接失败或 SecurityException。
   - 建议：集中封装蓝牙权限网关和连接状态机，替换 Activity Result API；每个 BLE 操作前由同一入口校验权限、蓝牙开关、设备连接状态。

5. 多处强制非空可能造成运行期崩溃。
   - 证据：`HotmeltPinter.kt:82/95/105/113` 强制使用 `portManager!!`；`BlueToothKit.kt:376/383/390` 强制使用 `currentGatt!!`；`HistoryRecordDetailActivity.kt:108/292`、`SystemSettingActivity.kt:126`、`NavBar.kt:91`、`DatabaseBackupHelperKit.kt:172/176/257/260/340/343` 也存在 `!!`。
   - 风险：蓝牙断连、打印 SDK 初始化失败、用户取消文件选择、历史记录无 PDF 等正常路径都可能触发崩溃。
   - 建议：把这些点改成显式状态分支和用户可见错误；打印/蓝牙模块应返回结构化结果，而不是依赖 nullable 全局对象。

6. 协程和异步任务不绑定生命周期。
   - 证据：`SearchActivity.kt:84`、`HotmeltPinter.kt:227` 使用 `GlobalScope.launch`；`PDFKit.kt:210` 仍基于 `AsyncTask`。
   - 风险：Activity 销毁后任务继续运行，可能泄漏上下文、重复回调、写入已失效 UI 或产生后台资源占用。
   - 建议：迁移到 `lifecycleScope`、`viewModelScope` 或明确的应用级 CoroutineScope；PDF 生成改为可取消的后台任务，并把输出路径、错误原因、进度状态结构化。

7. Room schema 和迁移可追踪性不足。
   - 证据：`LocalStorageKit.kt:246-247` 当前数据库 `version = 2` 且 `exportSchema = false`；但 `app/build.gradle.kts:57` 配置了 `room.schemaLocation`，仓库只有 `app/schemas/.../1.json`。
   - 风险：schema v2 未导出，迁移 review 和回归测试缺少基准；后续字段调整容易破坏历史数据。
   - 建议：打开 schema 导出并提交 v2 schema；为 `MIGRATION_1_2` 增加迁移测试，覆盖已有患者、记录、波形 chunk 和 PDF 路径字段。

8. 备份/恢复链路处于半完成状态。
   - 证据：`CapnoEasyApplication.kt:37/44` 备份恢复入口仍是 TODO/临时注释；`DatabaseBackupHelperKit.kt:367` 标注“先将数据放到 download 目录”；同文件存在大量 `println`、`runBlocking` 风格流程和 URI 强制非空。
   - 风险：真实用户数据恢复路径不可验证，首次安装、升级、卸载重装、权限拒绝时可能丢数据或阻塞启动流程。
   - 建议：先定义备份策略和权限策略，再实现可重复的导出/导入用例；恢复流程应有事务边界、失败回滚和用户确认。

## P2：中等风险问题

9. PDF 报告仍缺少可测试的版式边界。
   - 证据：`PDFKit.kt` 已增长到 1154 行，报告页面、趋势图、异常片段、水印、footer 都集中在同一个任务类；分页依赖 `templateConfig.*SectionHeight` 这类估算值。
   - 风险：字体、字段数量、异常片段数量变化时，PDF 容易出现挤压、跨页不自然或 footer 位置异常；当前又缺少 PDF 视觉回归。
   - 建议：拆分 header、patient info、summary、waveform、footer 渲染器；增加固定输入数据生成 PDF 的快照/文本提取测试，并至少保留一组多页异常片段样例。

10. 核心类过大，职责集中。
    - 证据：`BlueToothKit.kt` 1583 行，`PDFKit.kt` 1154 行，`LocalStorageKit.kt` 673 行，`AppStateModel.kt` 576 行，`HistoryRecordDetailActivity.kt` 521 行，`HotmeltPinter.kt` 432 行。
    - 风险：协议解析、状态管理、UI 回调、存储和调试逻辑耦合，后续修复容易产生连锁回归。
    - 建议：优先按边界拆分：BLE 扫描/连接/协议解析/采样流，PDF 数据准备/版式渲染/文件输出，Room DAO/Repository/偏好设置。

11. 构建依赖存在重复、陈旧和未证实依赖。
    - 证据：`app/build.gradle.kts:82-85` 重复声明 `foundation-layout-android`；`hotmeltprint/build.gradle.kts:42-44` 重复声明 `mpandroidchart`；`hotmeltprint/build.gradle.kts:50-51` 既 fileTree 又单独引入 `SDKLib.jar`；`app/build.gradle.kts:87` 使用 `hilt-navigation-compose:1.1.0-alpha01`；`app/build.gradle.kts:115` 引入 `androidx.databinding:baseLibrary:3.2.0-alpha11` 但未看到 dataBinding 配置。
    - 风险：依赖解析变慢、冲突难定位、未来 Gradle/AGP 升级成本增加。
    - 建议：统一使用 version catalog，删除重复和未使用依赖；对本地 jar/aar 写清来源、版本和校验方式。

12. 生产调试输出较多。
    - 证据：`rg` 扫描到大量 `println("wswTest...")`、`printStackTrace()`、临时 TODO，集中在 `BlueToothKit.kt`、`PDFKit.kt`、`HistoryRecordDetailActivity.kt`、`DatabaseBackupHelperKit.kt`、`CapnoEasyApplication.kt`。
    - 风险：日志噪声影响问题定位，可能暴露患者数据、设备状态或文件路径。
    - 建议：建立统一 logger，区分 debug/release；release 默认关闭敏感日志，异常要带结构化错误码而不是直接打印堆栈。

13. 测试覆盖基本为空。
    - 证据：`app` 和 `hotmeltprint` 的 test/androidTest 只有 4 个模板文件，共 78 行。
    - 风险：PDF 时间轴、Room migration、蓝牙协议解析、打印错误处理、权限流程都没有自动回归保护。
    - 建议：先补不依赖真机的纯逻辑测试：CO2 协议解析、异常窗口切分、PDF 时间轴分段、Room migration；再逐步补仪器化测试。

14. Gradle/Kotlin 工具链有升级压力。
    - 证据：编译输出提示 Kapt 不支持 Kotlin language version 2.0+，会回退到 1.9；Gradle 输出提示 deprecated features 将不兼容 Gradle 9.0。
    - 风险：未来升级 AGP/Gradle/Kotlin 时会被 kapt、旧插件或脚本 API 阻塞。
    - 建议：短期锁定可构建版本并记录；中期迁移 Room/Hilt 到 KSP 或确认 kapt 支持矩阵，清理 deprecated Gradle API。

## P3：低风险但建议整理

15. 命名和拼写不一致会增加维护成本。
    - 证据：`HotmeltPinter`、`DatabaserMigration_FROM1_TO2`、`Protocal`、`Atribute` 等命名不统一。
    - 风险：搜索和上下文映射更容易漏项，新增成员理解成本高。
    - 建议：稳定功能后分批重命名，并同步 context/entity mapping。

16. 本地构建体验仍依赖缓存和机器状态。
    - 证据：离线 lint 缺少 `lint-gradle:31.8.0`，之前也出现过离线单元测试缺少 JUnit 缓存的问题。
    - 风险：新机器或 CI 上“离线可构建”不可重复，排查会被缓存状态干扰。
    - 建议：明确哪些任务支持离线，哪些必须联网；CI 用干净缓存验证一次完整 build/test/lint。

## 建议处理顺序

1. 先同步 `PdfReportTemplateConfig` 相关 context 文档，消除源码和 context 漂移。
2. 运行最小验证闭环：`compileDebugKotlin`、unit test、lintDebug、debug 安装和一次 PDF 生成。
3. 处理 Manifest 权限、Bugly debug、`allowBackup` 和蓝牙权限网关。
4. 补 Room schema v2、迁移测试和 PDF 分段/异常窗口单元测试。
5. 分阶段拆分 `BlueToothKit`、`PDFKit`、`LocalStorageKit`，每阶段保持可构建和 context 同步。
