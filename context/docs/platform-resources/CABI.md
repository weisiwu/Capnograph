# CABI

来源批次：CapnoGraph Android Docker 打包环境补充。
定位入口：`context/entity-id-mapping.md`。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/CABI.md`。

## 实体定位

- 实体：Android builder Docker image
- ID / 别名：CABI, android-builder, Docker Compose, `wei123098/capnograph-android-builder:android-35-agp-8.8.0`
- 源文件：`compose.yaml`, `docker/android-builder/Dockerfile`, `docker/android-builder/README.md`
- 关联总览文档：`README.md`
- 备注：Android 打包镜像依赖和 Docker Compose 入口。

## 补充职责

CABI 固定 CapnoGraph Android 打包所需的外部 Docker 镜像，避免迁移到新机器或 CI 时依赖本机 JDK / Android SDK 状态。根目录 `compose.yaml` 把该镜像声明为 `android-builder` 服务，并挂载当前仓库到 `/workspace`。

## 关键 ID / 别名

CABI, android-builder, Docker Compose, `wei123098/capnograph-android-builder:android-35-agp-8.8.0`

## 关键字段 / 方法

- 镜像：`wei123098/capnograph-android-builder:android-35-agp-8.8.0`
- Compose 服务：`android-builder`
- 平台：`linux/amd64`
- Compose 入口：`entrypoint: []`，覆盖镜像默认 `bash -lc` 入口以保留脚本参数。
- 工作目录：`/workspace`
- Gradle 缓存卷：`capnograph-gradle-cache:/home/builder/.gradle`

## 主要调用点

根目录执行：

```bash
docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --no-daemon
```

Release 构建将 `--variant debug` 改为 `--variant release`。

## 注意事项

镜像只提供 Android 打包环境，不复制项目源码；源码通过运行时 volume 挂载。镜像当前为 `linux/amd64`，Apple Silicon 本机运行时会走 Docker 的 amd64 仿真。生产 release 签名仍需在 Android Gradle 配置中补环境变量驱动的签名配置。

直接使用 `docker run` 覆盖命令时，需要把完整脚本命令作为一个 shell 字符串传给镜像默认 `bash -lc` 入口；根目录 `compose.yaml` 已通过清空 entrypoint 避免这个细节泄漏到 Compose 调用方。

## 最小验证方式

`docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --dry-run --no-daemon`

## 同步要求

- 如果镜像名、tag、Compose 服务名、挂载路径或打包入口发生变化，同步更新本文档、`context/entity-id-mapping.md`、根 `README.md` 和 `docker/android-builder/README.md`。
- 如果 Dockerfile 的 SDK / JDK / build-tools 版本变化，同步更新镜像 tag 和依赖说明。
