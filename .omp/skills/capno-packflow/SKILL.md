---
name: capno-packflow
description: Android/iOS 打包流水线与飞书通知。用于 OMP 触发打包、收集产物、并通过 Feishu webhook 通知。
globs:
  - "scripts/package.sh"
  - "scripts/*.sh"
  - "compose.yaml"
  - "docker/**"
  - ".omp/tools/capno-packflow.js"
---

# CapnoGraph PackFlow Skill

Use this skill when the user asks to run a build pipeline, collect APK/AAB/IPA artifacts, trigger Packflow, or notify Feishu.

## Entrypoint Tools

- `capno_packflow_bot_command`：飞书消息口令路由。输入用户原始文本，映射到打包、最新状态、历史记录、详情、配置或帮助。
- `capno_packflow_agent`：推荐给 AI/机器人调用的 Packflow 后台入口。触发本机 Packflow agent API，等待构建完成，读取 Packflow 产物，并可通知飞书机器人 `CapnoGraph OMP Bot`；Android APK 大文件通过 Packflow 公开 token 下载链接交付，不上传文件二进制到飞书。
- `capno_packflow_query`：查询 Packflow 下 CapnoGraph 的项目配置、最新构建、构建历史和指定 build 详情。
- `capno_packflow`：执行打包流程。支持 `run` 开关（默认只预览），支持本地和 Docker 两种方式，采集产物并返回路径清单。
- `capno_feishu_send`：向飞书 webhook 发送打包结果与产物摘要（当前为文本推送，不承载文件二进制）。

## 默认行为

- `capno_packflow_agent` 默认 Packflow 项目：`CapnoGraph`
- `capno_packflow_agent` 默认 Packflow 配置：`Android Debug APK`
- `capno_packflow_agent` 默认分支：`monorepo_v1`
- Android 默认打包目标：`android`
- 默认构建变体：`release`
- Android 默认方式：Docker（`compose.yaml` 的 `android-builder`）
- 打包成功后可开启 `notifyOnSuccess` 自动推送消息到飞书。

## 常用参数

- `target`: `android` 或 `ios`
- `variant`: `debug` / `release`
- `useDocker`: Android 是否走 Docker 流程
- `collectArtifacts`: 是否收集 `apps/android/app/build/outputs` 下的产物
- `includeChecksums`: 是否返回 SHA-256（大文件建议关闭）
- `notifyWebhookUrl` / `feishuWebhookUrl`: 飞书 incoming webhook 地址（优先通过 `CAPNOGRAPH_OMP_BOT_WEBHOOK_URL`，也支持 `FEISHU_WEBHOOK_URL` 和 `FEISHU_WEBHOOK` 环境变量兜底）
- `notifyWebhookSecret` / `feishuWebhookSecret`: 飞书签名密钥（可通过 `CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET` 或 `FEISHU_WEBHOOK_SECRET` 兜底）
- `includeArtifactDownloadLinks`: 默认 true；在飞书通知中加入 Packflow `/api/public/artifacts/<artifactId>/download?token=<token>` 公开下载链接。
- `packflowPublicBaseUrl`: 公网 Packflow 地址；也可用 `CAPNOGRAPH_PACKFLOW_PUBLIC_BASE_URL` / `PACKFLOW_PUBLIC_BASE_URL`。本机默认可从 `~/.cloudflared/packflow-baoganai.yml` 发现 `https://packflow.baoganai.com`；给飞书发送大 APK 时必须有公网地址，工具不会再回退到 `localhost`。
- `packflowCloudflaredConfigPath`: 可选 cloudflared 配置路径；也可用 `CAPNOGRAPH_PACKFLOW_CLOUDFLARED_CONFIG` / `PACKFLOW_CLOUDFLARED_CONFIG`。
- `packflowPublicDownloadSecret`: 可选公开下载 HMAC 密钥；也可用 `CAPNOGRAPH_PACKFLOW_PUBLIC_DOWNLOAD_SECRET` / `PACKFLOW_PUBLIC_DOWNLOAD_SECRET`。如果 Packflow 服务端未设置该密钥，token 使用产物 SHA-256。
- Android APK 产物会在 Gradle 构建后重命名为 `app-<variant>-v<version>-<yyyyMMdd-HHmmss>.apk`，例如 `app-debug-v1.2-20260624-153012.apk`。

## 输出

打包工具会返回

- 执行命令与日志
- 收集到的产物列表（路径、大小、修改时间，若开启 checksum 也会返回 sha256）
- 通知结果（若开启 `notifyOnSuccess`）

## 对接建议（本机 OMP → 飞书）

在飞书机器人的工具清单中暴露 `capno_packflow_bot_command`，由它解析用户口令并路由：

1. 用户发 `打包` / `打 APK` / `打 Android 包` / `capno_packflow_agent`：路由到 `capno_packflow_agent`，触发 `Android Debug APK`。
2. 用户发 `打包状态` / `最新打包` / `packflow latest`：路由到 `capno_packflow_query(action=latest)`。
3. 用户发 `打包历史` / `最近 10 条打包` / `packflow history`：路由到 `capno_packflow_query(action=history)`。
4. 用户发 `打包详情 <buildId>` / `packflow build <buildId>`：路由到 `capno_packflow_query(action=detail)`。
5. 用户发 `打包配置` / `packflow info`：路由到 `capno_packflow_query(action=projects)`。
6. 用户发 `打 release 包`：返回拦截提示，不触发 release；release 仍在 R8/minify 阶段不稳定。

配置要点：

- 打包参数默认传 `projectName=CapnoGraph`, `configName=Android Debug APK`, `branch=monorepo_v1`。
- 配置 `waitForCompletion=true`, `timeoutSeconds=180`, `notifyFeishu=true`。
- webhook 使用环境变量，不要写入仓库：优先 `CAPNOGRAPH_OMP_BOT_WEBHOOK_URL`，兼容 `FEISHU_WEBHOOK_URL` / `FEISHU_WEBHOOK`。
- 如需自定义二次消息，可再调用 `capno_feishu_send`。当前 APK 约 101 MB，超过飞书文件上传上限；飞书里发送 Packflow 公网 token 下载链接、大小和 SHA-256，而不是文件本体。

已验证链路：`capno_packflow_agent` 触发 `Android Debug APK` build `f74cdf65-b3c6-4ad0-9ed4-2903f8ebe144`，Packflow 耗时 `25.090s`，产物 `apps/android/app/build/outputs/apk/debug/app-debug.apk`，飞书返回 `StatusCode=0` / `success`。2026-06-24 复验 build `df3a3a14-61db-48ba-811e-c87383e3566a` 耗时 `26.362s`，产物 `59d86d7d-a5c5-44b6-8a35-92e9991e80d3`，飞书补发通知成功；后续通知已改为公开 token 下载链接。
