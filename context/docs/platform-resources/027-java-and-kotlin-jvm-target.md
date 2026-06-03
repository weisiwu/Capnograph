# Java and Kotlin JVM target

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L60）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/027-java-and-kotlin-jvm-target.md`。

## 实体定位

- 实体：Java and Kotlin JVM target
- ID / 别名：`JavaVersion.VERSION_11`, `jvmTarget=11`, `org.gradle.java.home`
- 源文件：`gradle.properties`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`, `JDK_SETUP.md`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：模块目标字节码为 Java 11；机器相关 JDK 路径放到用户级 Gradle 配置

## 补充职责

控制模块 Java/Kotlin 字节码目标，并说明每台机器如何配置 Gradle 使用的 Java Home。

## 关键 ID / 别名

`JavaVersion.VERSION_11`, `jvmTarget=11`, `org.gradle.java.home`

## 关键字段 / 方法

两个模块 `sourceCompatibility`/`targetCompatibility` 为 `JavaVersion.VERSION_11`，Kotlin `jvmTarget="11"`；项目级 `gradle.properties` 不再配置 `org.gradle.java.home`。本机路径写入用户级 `~/.gradle/gradle.properties`，Windows 示例值保留在 `JDK_SETUP.md`：`org.gradle.java.home=C:\\Program Files\\Java\\jdk-17`。

## 主要调用点

Java/Kotlin 编译任务使用模块字节码目标；Gradle daemon 使用用户级 Gradle 配置、Android Studio Gradle JDK、`JAVA_HOME` 或命令行 `-Dorg.gradle.java.home=...` 解析本机 JDK。

## 注意事项

不要在项目级 `gradle.properties` 提交机器相关 JDK 绝对路径。本机已写入用户级 `~/.gradle/gradle.properties`：`org.gradle.java.home=/opt/homebrew/Cellar/openjdk@21/21.0.10/libexec/openjdk.jdk/Contents/Home`。Windows 机器按 `JDK_SETUP.md` 写入用户级配置并保留当前 Windows 值：`org.gradle.java.home=C:\\Program Files\\Java\\jdk-17`。

## 最小验证方式

`./gradlew --version` 与 `./gradlew :app:assembleDebug`；若用户级 Gradle 配置不可用，可临时使用 `-Dorg.gradle.java.home=<local JDK home>`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
