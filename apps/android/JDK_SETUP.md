# JDK 本地配置说明

本项目不要在根目录 `gradle.properties` 中提交机器相关的 JDK 绝对路径。不同机器的 JDK 安装路径不同，应放到各自机器的用户级 Gradle 配置、Android Studio Gradle JDK 设置、`JAVA_HOME`，或通过命令行临时覆盖。

## 推荐方式：用户级 Gradle 配置

在每台机器上创建或修改用户级 Gradle 配置：

```text
~/.gradle/gradle.properties
```

写入本机 JDK 路径：

```properties
org.gradle.java.home=/path/to/local/jdk
```

这个文件不在项目仓库中，不会影响其他开发机器。

## Windows 处理方式

Windows 机器继续保留当前项目原来使用的 JDK 路径值，但要写到用户级 Gradle 配置中，而不是项目根目录 `gradle.properties`：

```properties
org.gradle.java.home=C:\\Program Files\\Java\\jdk-17
```

通常文件位置是：

```text
C:\Users\<your-user>\.gradle\gradle.properties
```

## macOS 示例

Homebrew OpenJDK 的路径以实际安装位置为准。例如本机配置为：

```properties
org.gradle.java.home=/opt/homebrew/Cellar/openjdk@21/21.0.10/libexec/openjdk.jdk/Contents/Home
```

## 临时命令行覆盖

如果暂时不想改用户级配置，可以在命令行里覆盖：

```bash
./gradlew :app:assembleDebug -Dorg.gradle.java.home=/path/to/local/jdk
```

## 不要混淆 JDK 路径和字节码目标

模块里的这些配置是编译产物目标，不是本机 JDK 路径，应该继续保留：

- `sourceCompatibility = JavaVersion.VERSION_11`
- `targetCompatibility = JavaVersion.VERSION_11`
- `jvmTarget = "11"`

