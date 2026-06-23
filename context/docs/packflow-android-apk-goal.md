# Packflow Android APK Goal

本文档整理 CapnoGraph 当前通过 Packflow 打 Android APK 的问题、优化点和验证结果。最终目标是：通过 Packflow 为 CapnoGraph 产出 Android APK，并且 Packflow 记录的总耗时不超过 60 秒。

## 当前结论

- 已达成的快速路径：`Android Debug APK`
- Packflow 项目：`CapnoGraph`
- Packflow 配置：`Android Debug APK`
- 分支：`monorepo_v1`
- 构建命令：`docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --no-daemon`
- 产物路径：`apps/android/app/build/outputs/apk/debug/*.apk`
- 验证 build：`df3a3a14-61db-48ba-811e-c87383e3566a`
- Packflow 总耗时：`26.362s`
- Gradle 日志耗时：`23s`
- 产物命名：`apps/android/app/build/outputs/apk/debug/app-debug-v<version>-<yyyyMMdd-HHmmss>.apk`
- 产物大小：`105690613` bytes
- SHA-256：`15ab3ba39dff9b66aca24a40150e4a731eb8e67fab69bd41b6cb31b8dbedf730`
- AI/飞书链路：Packflow 已产出 debug APK；因 APK 体积超过飞书文件上传限制，飞书消息改为发送 Packflow 公开 token 下载链接，webhook 返回 `StatusCode=0` / `success`

`Android Debug APK` 的 install command 必须保持为空。此前保留 `docker compose version` 作为 install command 时，warm debug 构建为 `60.412s`，刚好超过 60 秒。

## 已验证构建记录

| Build ID | 配置 | 结果 | Packflow 总耗时 | 产物数 | 关键结论 |
| --- | --- | --- | ---: | ---: | --- |
| `2756f1fc-95e6-465e-bc05-132b4553d611` | Android Release APK | failed | `1.217s` | 0 | Docker daemon 未启动：`Cannot connect to the Docker daemon`。 |
| `4f691841-a57b-4bef-9e19-8107a508c722` | Android Release APK | failed | `1011.402s` | 0 | 进入 `:app:minifyReleaseWithR8` 后 Gradle daemon 消失。 |
| `1da52139-9803-4f9e-a496-0557a4751ebc` | Android Debug APK | success | `313.905s` | 1 | debug 冷启动成功，但新 workspace 和冷中间产物导致耗时 5 分钟以上。 |
| `e237eb1a-82df-44e0-9f5e-effca1ffc3f7` | Android Debug APK | success | `60.412s` | 1 | warm workspace/cache 成功，但 install command 额外开销使总耗时略超 60 秒。 |
| `6bb1f453-02ab-4b2f-8599-0c6565924701` | Android Debug APK | success | `29.043s` | 1 | 移除 install command 后，warm debug APK 达成 60 秒内目标。 |
| `bf882c32-67bb-4d7e-abcb-f08e974ea33f` | Android Debug APK | success | `26.096s` | 1 | `capno_packflow_agent` 测试 harness 成功触发 Packflow 并收集产物。 |
| `f74cdf65-b3c6-4ad0-9ed4-2903f8ebe144` | Android Debug APK | success | `25.090s` | 1 | `capno_packflow_agent` 端到端成功；飞书 `CapnoGraph OMP Bot` 通知返回 success。 |
| `df3a3a14-61db-48ba-811e-c87383e3566a` | Android Debug APK | success | `26.362s` | 1 | 2026-06-24 复验成功；产物 `59d86d7d-a5c5-44b6-8a35-92e9991e80d3`，大小 `105690613` bytes，SHA-256 `15ab3ba39dff9b66aca24a40150e4a731eb8e67fab69bd41b6cb31b8dbedf730`，飞书补发文本通知成功；后续通知已改为公开 token 下载链接。 |

## 当前问题

1. `Android Release APK` 仍不可用作 1 分钟目标路径。release 构建开启 `isMinifyEnabled=true` 和 `isShrinkResources=true`，会进入 R8；在 Docker builder 里耗时很长，并在 `:app:minifyReleaseWithR8` 阶段出现 `Gradle build daemon disappeared unexpectedly`。
2. Docker builder 固定为 `linux/amd64`。Apple Silicon 本机通过 Docker/Rosetta 跑 amd64 Java/Kotlin/AGP，冷启动和全量编译明显慢于 native。
3. `--no-daemon` 加上 `docker compose run --rm` 意味着每次构建都会创建容器和单次 Gradle daemon；只能复用 Gradle 缓存和 workspace 中间产物，不能复用常驻 daemon。
4. Packflow 的 install command 会计入总耗时。`docker compose version` 这类健康检查在 warm debug 路径上足以把总耗时从 60 秒内推到 60 秒以上。
5. 60 秒目标依赖 warm workspace/cache。首次 clone 或首次 debug 中间产物生成仍会超过 60 秒。

## 优化点

- 已应用：新增 `Android Debug APK` Packflow 配置，作为 60 秒内快速 APK 验证路径。
- 已应用：清空 `Android Debug APK` 的 install command，避免每次重复执行 Docker Compose 版本检查。
- 已应用：`capno_packflow_agent` 在返回结果和飞书通知中加入 Packflow 产物下载链接，Android APK 大文件不再尝试作为飞书文件上传。
- 已应用：Android APK 在 `scripts/package.sh` 构建成功后重命名为 `app-<variant>-v<version>-<yyyyMMdd-HHmmss>.apk`，Packflow 的 `*.apk` 产物规则会收集带版本和时间的文件名。
- 可选：如果必须让 release APK 可用，优先把 release 目标拆成 `fastRelease` 或关闭 fast 包的 R8/shrink；生产 release 仍保留 R8。
- 可选：构建 host-native Packflow 配置，直接在 macOS/arm64 上运行 `apps/android/gradlew :app:assembleDebug`，减少 amd64 Docker 仿真开销。
- 可选：制作 arm64 Android builder 镜像或 multi-arch builder，替代当前 `linux/amd64` 镜像。
- 可选：用长驻 builder 服务复用 Gradle daemon，而不是每次 `docker compose run --rm` 创建一次容器。
- 可选：提升 Gradle daemon 内存，例如把 `org.gradle.jvmargs=-Xmx2048m` 调高到 4g/6g，再验证 release R8 是否仍崩溃。

## 操作准则

- 快速 APK 验证使用 `Android Debug APK`。
- 生产 release 交付不要用当前失败的 `Android Release APK` 结论冒充成功；需要单独修复 R8/内存/签名和耗时。
- 飞书机器人原始文本先交给 `capno_packflow_bot_command`。已映射口令包括：`打包`、`打 APK`、`打包状态`、`打包历史`、`最近 10 条打包`、`打包详情 <buildId>`、`打包配置`、`打包帮助`。`打 release 包` 会被拦截，不触发 release。
- AI/机器人明确触发 Packflow build 时使用 OMP 工具 `capno_packflow_agent`，显式传入：

```json
{
  "projectName": "CapnoGraph",
  "configName": "Android Debug APK",
  "branch": "monorepo_v1",
  "includeArtifactDownloadLinks": true,
  "packflowPublicBaseUrl": "https://packflow.baoganai.com"
}
```

如需飞书通知，设置环境变量 `CAPNOGRAPH_OMP_BOT_WEBHOOK_URL`（兼容 `FEISHU_WEBHOOK_URL` / `FEISHU_WEBHOOK`）并传 `notifyFeishu=true`。签名模式使用 `CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET` 或 `FEISHU_WEBHOOK_SECRET`，不要把 webhook 或 secret 写进仓库。

当前 debug APK 大约 `105690613` bytes，超过飞书文件上传限制；不要让机器人尝试上传 APK 文件本体。`capno_packflow_agent` 默认把 Packflow 公开 token 下载链接 `/api/public/artifacts/<artifactId>/download?token=<token>` 写入飞书文本通知。若收件人不在 Packflow 同机环境，必须设置 `CAPNOGRAPH_PACKFLOW_PUBLIC_BASE_URL` 或 `PACKFLOW_PUBLIC_BASE_URL` 指向可访问的 Packflow 公网地址；本机可从 `~/.cloudflared/packflow-baoganai.yml` 自动发现 `https://packflow.baoganai.com`；未配置且无法发现时工具会失败而不是发送 `localhost` 链接。

## 复验命令

```bash
sqlite3 /Users/weisiwu_clawbot_mac/Desktop/work/github/packflow/data/packflow.db \
  "select id, build_number, status, duration_ms, artifact_count from builds where project_id='db2e326e-5369-4cea-8164-017cd7a17da9' order by build_number desc limit 5;"
```

`duration_ms < 60000` 且 `artifact_count = 1` 才能证明 1 分钟内 Packflow APK 目标达成。

端到端 AI/飞书链路还应确认 OMP 返回的 `details.artifacts[0].downloadUrl`、`details.notification.sent=true`，且飞书 webhook 响应 `StatusCode=0` 或 `code=0`。

口令路由验证可使用 `capno_packflow_bot_command` 的 `dryRun=true`，避免测试时误触发真实构建。

2026-06-24 复验问题记录：一次本地 Node 监控 harness 在构建已接受后把 `null` 传给 `child_process.spawn({ signal })`，导致监控脚本自身抛出 `ERR_INVALID_ARG_TYPE`，自动通知未执行；Packflow 构建未受影响，随后通过 SQLite 继续监控并用 `capno_feishu_send` 补发飞书通知。后续 harness 应只在 `signal` 为 `AbortSignal` 时传入 `spawn`。
