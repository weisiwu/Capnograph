# PACKFLOW

来源批次：CapnoGraph Packflow 后台接入。
定位入口：`context/entity-id-mapping.md`。
领域：构建平台。
实体级上下文：`context/docs/platform-resources/PACKFLOW.md`。

## 实体定位

- 实体：Packflow backend configs
- ID / 别名：Packflow, packflow backend, `CapnoGraph`, `Android Release APK`, `Android Debug APK`, `iOS Release Archive`, `capno_packflow_agent`, `capno_packflow_bot_command`, `capno_packflow_query`, `CapnoGraph OMP Bot`
- 源文件：`README.md`, `scripts/package.sh`, `compose.yaml`, `.omp/tools/capno-packflow.js`, `.omp/skills/capno-packflow/SKILL.md`
- 外部运行时：本机 Packflow 后台 `http://localhost:3001`
- 备注：Packflow 后台配置保存在本机 Packflow 数据库，不属于本仓库源码文件。

## 补充职责

Packflow 负责把 CapnoGraph 的本地打包命令登记为可由 UI 或 agent tools 触发的构建配置。当前项目名为 `CapnoGraph`，包含 Android release APK、Android debug APK 和 iOS release archive 配置。

## 关键 ID / 别名

Packflow, packflow backend, `CapnoGraph`, `Android Release APK`, `Android Debug APK`, `iOS Release Archive`, `capno_packflow_agent`, `capno_packflow_bot_command`, `capno_packflow_query`, `CapnoGraph OMP Bot`

## 关键字段 / 方法

| 配置 | 平台 | workdir | install command | build command | artifact path |
| --- | --- | --- | --- | --- | --- |
| Android Release APK | android | `.` | `docker compose version` | `docker compose run --rm android-builder scripts/package.sh --target android --variant release -- --no-daemon` | `apps/android/app/build/outputs/apk/release/*.apk` |
| Android Debug APK | android | `.` | 空 | `docker compose run --rm android-builder scripts/package.sh --target android --variant debug -- --no-daemon` | `apps/android/app/build/outputs/apk/debug/*.apk` |
| iOS Release Archive | ios | `.` | `xcodebuild -version` | `scripts/package.sh --target ios --variant release` | `apps/ios/build/*.xcarchive/**` |

## 主要调用点

Packflow agent tools 暴露在本机后台：

- `GET http://localhost:3001/api/agent/tools`
- `GET http://localhost:3001/api/agent/openapi`
- `POST http://localhost:3001/api/agent/tools/start-build`

启动当前仓库分支构建时显式传入：

```json
{
  "projectName": "CapnoGraph",
  "configName": "Android Debug APK",
  "branch": "monorepo_v1"
}
```

iOS 构建把 `configName` 改为 `iOS Release Archive`。

AI/机器人调用不要让机器人直接拼 shell 命令。飞书原始文本优先走 OMP 工具 `capno_packflow_bot_command`；如果后台已经明确知道要打包，则直接走 `capno_packflow_agent`。`capno_packflow_agent` 会调用 Packflow agent API、轮询 Packflow SQLite 构建状态、读取 artifacts 表，并可通过飞书 incoming webhook 通知 `CapnoGraph OMP Bot`。推荐打包参数：

```json
{
  "projectName": "CapnoGraph",
  "configName": "Android Debug APK",
  "branch": "monorepo_v1",
  "waitForCompletion": true,
  "timeoutSeconds": 180,
  "notifyFeishu": true,
  "includeArtifactDownloadLinks": true,
  "packflowPublicBaseUrl": "https://packflow.baoganai.com"
}
```

飞书 webhook 使用环境变量，不写入仓库：优先 `CAPNOGRAPH_OMP_BOT_WEBHOOK_URL`，兼容 `FEISHU_WEBHOOK_URL` 和 `FEISHU_WEBHOOK`；签名密钥使用 `CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET` 或 `FEISHU_WEBHOOK_SECRET`。

Android debug APK 当前约 `105690613` bytes，飞书通知不要上传 APK 文件本体。`capno_packflow_agent` 默认发送 Packflow 公开 token 下载链接 `/api/public/artifacts/<artifactId>/download?token=<token>`；跨机器或外网收件人必须通过 `CAPNOGRAPH_PACKFLOW_PUBLIC_BASE_URL` / `PACKFLOW_PUBLIC_BASE_URL` 配置可访问的 Packflow 地址，或让工具从本机 `~/.cloudflared/packflow-baoganai.yml` 自动发现 `https://packflow.baoganai.com`，缺少公网地址时工具会失败而不是发送 `localhost` 链接。若 Packflow 服务端设置 `PACKFLOW_PUBLIC_DOWNLOAD_SECRET`，通知端也要设置同值；否则 token 使用产物 SHA-256。

飞书口令路由：

| 用户文本 | 路由 |
| --- | --- |
| `打包`, `打 APK`, `打 Android 包`, `capno_packflow_agent` | `capno_packflow_agent`，触发 `Android Debug APK` |
| `打包状态`, `最新打包`, `packflow latest` | `capno_packflow_query(action=latest)` |
| `打包历史`, `最近 10 条打包`, `packflow history` | `capno_packflow_query(action=history)` |
| `打包详情 <buildId>`, `packflow build <buildId>` | `capno_packflow_query(action=detail)` |
| `打包配置`, `packflow info` | `capno_packflow_query(action=projects)` |
| `打包帮助`, `packflow help` | 返回口令帮助 |
| `打 release 包` | 拦截提示，不触发 release |

## 注意事项

本仓库当前工作分支是 `monorepo_v1`。Packflow UI/agent 默认分支可能是 `main`，触发构建时需要显式指定 `branch=monorepo_v1`，除非 Packflow 项目默认分支已经改成当前分支。

Android 配置依赖根目录 `compose.yaml` 中的 `android-builder` 服务和 `scripts/package.sh`；iOS 配置依赖本机 Xcode、签名资产和 `apps/ios/CapnoGraph.xcodeproj`。Packflow 登录凭据、session cookie、飞书 webhook 和密钥不要写入仓库；Packflow DB 路径跨机器不同时用 `PACKFLOW_DB_PATH` 覆盖。

`Android Debug APK` 是当前达成 60 秒内 APK 目标的快速路径。验证 build `6bb1f453-02ab-4b2f-8599-0c6565924701` 的 Packflow 总耗时为 `29.043s`，产物为 `apps/android/app/build/outputs/apk/debug/app-debug.apk`；新构建会在 Gradle 结束后重命名为 `app-<variant>-v<version>-<yyyyMMdd-HHmmss>.apk`。AI/飞书链路验证 build `f74cdf65-b3c6-4ad0-9ed4-2903f8ebe144` 的 Packflow 总耗时为 `25.090s`，产物大小 `105690613` bytes，SHA-256 为 `0705f73d1904c8560a3253ae53cefe692248f69727fdbde182675f9f0aff1cad`，飞书响应 `StatusCode=0` / `success`。2026-06-24 复验 build `df3a3a14-61db-48ba-811e-c87383e3566a` 总耗时 `26.362s`，产物 `59d86d7d-a5c5-44b6-8a35-92e9991e80d3`，SHA-256 `15ab3ba39dff9b66aca24a40150e4a731eb8e67fab69bd41b6cb31b8dbedf730`，飞书改发 Packflow 下载链接后 webhook 成功。`Android Release APK` 仍在 R8/minify 阶段失败，不应作为已达成目标记录。

## 最小验证方式

```bash
curl -sS http://localhost:3001/api/agent/tools
sqlite3 /Users/weisiwu_clawbot_mac/Desktop/work/github/packflow/data/packflow.db \
  "select project_name, name, platform, branch, workdir, build_command, artifact_path from projects where project_name='CapnoGraph';"
```

上述验证只确认 Packflow 后台工具接口和项目配置存在，不触发真实打包。真实构建会拉起 Android Docker 或 Xcode archive，应按需手动触发。

Android APK 目标、失败记录和优化清单见 `context/docs/packflow-android-apk-goal.md`。

`capno_packflow_agent` 最小端到端验证可在 OMP 或测试 harness 中触发 `Android Debug APK`，并确认返回的 `details.build.status=success`、`details.artifacts[0].file_name` 匹配 `apps/android/app/build/outputs/apk/debug/app-debug-v<version>-<yyyyMMdd-HHmmss>.apk`、`details.artifacts[0].downloadUrl` 使用 `/api/public/artifacts/`、`details.notification.sent=true`。`capno_packflow_bot_command` 的无副作用验证用 `dryRun=true` 测试 `capno_packflow_agent`、`打包状态`、`最近 3 条打包`、`打包详情 <buildId>`、`打包配置`、`打 release 包` 等口令。

## 同步要求

- 如果 Packflow 项目名、配置名、分支、构建命令或产物路径变化，同步更新本文档、`README.md`、`context/entity-id-mapping.md`、`context/docs/build-platform.md` 和 `context/docs/platform-resources.md`。
- 如果 `scripts/package.sh`、`compose.yaml` 或 iOS archive 输出路径变化，同步更新 Packflow 后台配置。
