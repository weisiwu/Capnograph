# Gradle wrapper

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L48）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/015-gradle-wrapper.md`。

## 实体定位

- 实体：Gradle wrapper
- ID / 别名：`./gradlew`, `gradle-8.10.2-all`, Gradle 8.10.2
- 源文件：`gradlew`, `gradle/wrapper/gradle-wrapper.properties`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：本地构建和测试入口；wrapper 使用华为云 Gradle 8.10.2 all 包

## 补充职责

统一本地构建、测试、依赖解析和 Gradle 任务执行入口。

## 关键 ID / 别名

`./gradlew`, `gradle-8.10.2-all`, Gradle 8.10.2

## 关键字段 / 方法

`gradle/wrapper/gradle-wrapper.properties` 指向 `https://repo.huaweicloud.com/gradle/gradle-8.10.2-all.zip`；wrapper 脚本为 `./gradlew`。

## 主要调用点

所有推荐验证命令都通过 wrapper 执行，避免依赖本机全局 Gradle 版本。

## 注意事项

同名实体在定位表中出现两次：L48 是构建平台能力，L510 是构建/测试入口；两个实体文档分别记录不同语境。`./gradlew` 在 Git 索引中应保持 `100755` 可执行模式；首次执行会下载 Gradle 8.10.2 到本机 `~/.gradle/wrapper/dists`，依赖安装/构建会继续写入 `~/.gradle` 缓存。若脚本缺少执行位，先在仓库根目录执行 `chmod +x gradlew`。

## 最小验证方式

`./gradlew --version`；安装依赖并验证 Debug 构建可执行 `./gradlew :app:assembleDebug`。本机 JDK 路径应按 `JDK_SETUP.md` 写入用户级 Gradle 配置；临时构建可用 `-Dorg.gradle.java.home=<local JDK home>` 覆盖。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
