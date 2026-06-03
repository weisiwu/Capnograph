# Java and Kotlin JVM target

来源任务：`temp-task-list-1-platform-resources.md`。
定位入口：`context/entity-id-mapping.md`（L60）。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/027-java-and-kotlin-jvm-target.md`。

## 实体定位

- 实体：Java and Kotlin JVM target
- ID / 别名：`JavaVersion.VERSION_11`, `jvmTarget=11`, `org.gradle.java.home`
- 源文件：`gradle.properties`, `app/build.gradle.kts`, `hotmeltprint/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：模块目标字节码为 Java 11；Gradle 属性里配置了 JDK 17 路径

## 补充职责

控制模块 Java/Kotlin 字节码目标和 Gradle 使用的 Java Home。

## 关键 ID / 别名

`JavaVersion.VERSION_11`, `jvmTarget=11`, `org.gradle.java.home`

## 关键字段 / 方法

两个模块 `sourceCompatibility`/`targetCompatibility` 为 `JavaVersion.VERSION_11`，Kotlin `jvmTarget="11"`；`gradle.properties` 配置 `org.gradle.java.home=C:\Program Files\Java\jdk-17`。

## 主要调用点

Java/Kotlin 编译任务和 Gradle daemon 使用这些设置。

## 注意事项

该 Java Home 是 Windows 路径；在 macOS/Linux 环境需要本机 Gradle/JDK 配置覆盖。本机依赖安装验证使用 Homebrew OpenJDK 21，并通过命令行参数覆盖：`-Dorg.gradle.java.home=/opt/homebrew/Cellar/openjdk@21/21.0.10/libexec/openjdk.jdk/Contents/Home`。跨机器执行时替换为实际 JDK home。

## 最小验证方式

`./gradlew --version -Dorg.gradle.java.home=<local JDK home>` 与 `./gradlew :app:assembleDebug -Dorg.gradle.java.home=<local JDK home>`。

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
