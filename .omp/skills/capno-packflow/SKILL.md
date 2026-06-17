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

Use this skill when the user asks to run a build pipeline, collect APK/AAB/IPA artifacts, or notify Feishu.

## Entrypoint Tools

- `capno_packflow`：执行打包流程。支持 `run` 开关（默认只预览），支持本地和 Docker 两种方式，采集产物并返回路径清单。
- `capno_feishu_send`：向飞书 webhook 发送打包结果与产物摘要（当前为文本推送，不承载文件二进制）。

## 默认行为

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
- `notifyWebhookUrl`: 飞书 incoming webhook 地址（可通过 `FEISHU_WEBHOOK_URL` 环境变量兜底）

## 输出

打包工具会返回

- 执行命令与日志
- 收集到的产物列表（路径、大小、修改时间，若开启 checksum 也会返回 sha256）
- 通知结果（若开启 `notifyOnSuccess`）

## 对接建议（本机 OMP → 飞书）

在飞书机器人的工具清单中暴露 `capno_packflow`：

1. 只传 `target=android`, `variant=release`, `run=true`。
2. 配置 `notifyOnSuccess=true`、`notifyWebhookUrl=<incoming_webhook>`。
3. 或者先执行 `capno_packflow`（`run=true`），再用 `capno_feishu_send` 使用返回的 `artifacts.relativePath` 做二次发送（仅文本方式，文件本体需你再补一层文件分发能力）。
