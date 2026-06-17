const crypto = require("crypto");
const fs = require("fs");
const path = require("path");

const DEFAULT_MAX_ARTIFACTS = 20;

function resolveWebhook(baseUrl, secret) {
  if (!baseUrl) {
    return null;
  }

  if (!secret) {
    return baseUrl;
  }

  const timestamp = Date.now().toString();
  const sign = crypto
    .createHmac("sha256", secret)
    .update(`${timestamp}\n${secret}`)
    .digest("base64");

  const separator = baseUrl.includes("?") ? "&" : "?";
  return `${baseUrl}${separator}timestamp=${encodeURIComponent(timestamp)}&sign=${encodeURIComponent(sign)}`;
}

function formatSize(bytes) {
  if (!Number.isFinite(bytes) || bytes <= 0) {
    return "0 B";
  }

  const units = ["B", "KB", "MB", "GB"];
  let size = bytes;
  let unitIndex = 0;

  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024;
    unitIndex += 1;
  }

  return `${size.toFixed(size >= 10 || unitIndex === 0 ? 0 : 1)} ${units[unitIndex]}`;
}

function walkFiles(rootDir, targetDir, extensions) {
  const base = path.join(rootDir, targetDir);
  if (!fs.existsSync(base)) {
    return [];
  }

  const found = [];
  const stack = [base];

  while (stack.length > 0) {
    const current = stack.pop();
    for (const entry of fs.readdirSync(current, { withFileTypes: true })) {
      const absPath = path.join(current, entry.name);
      if (entry.isDirectory()) {
        stack.push(absPath);
        continue;
      }

      if (!entry.isFile()) {
        continue;
      }

      const normalized = path.extname(entry.name).toLowerCase();
      if (extensions.includes(normalized)) {
        found.push(absPath);
      }
    }
  }

  return found;
}

function buildArtifactCandidates(target, variant) {
  if (target === "android") {
    const base = "apps/android/app/build/outputs";
    return [
      { dir: `${base}/apk/${variant}`, extensions: [".apk"], label: "apk" },
      { dir: `${base}/bundle/${variant}`, extensions: [".aab"], label: "aab" },
      { dir: `${base}/mapping/${variant}`, extensions: [".txt"], label: "mapping" },
    ];
  }

  return [
    {
      dir: "apps/ios/build",
      extensions: [".ipa", ".zip"],
      label: "ios",
    },
  ];
}

function collectArtifacts(root, target, variant, includeChecksums = false) {
  const candidates = buildArtifactCandidates(target, variant);
  const collected = [];

  for (const candidate of candidates) {
    for (const absPath of walkFiles(root, candidate.dir, candidate.extensions)) {
      const stat = fs.statSync(absPath);
      const relPath = path.relative(root, absPath);
      const detail = {
        type: candidate.label,
        path: absPath,
        relativePath: relPath,
        size: stat.size,
        sizeLabel: formatSize(stat.size),
        modifiedAt: new Date(stat.mtimeMs).toISOString(),
      };

      if (includeChecksums) {
        const hash = crypto.createHash("sha256");
        hash.update(fs.readFileSync(absPath));
        detail.sha256 = hash.digest("hex");
      }

      collected.push(detail);
    }
  }

  collected.sort((a, b) => b.modifiedAt.localeCompare(a.modifiedAt));
  return collected.slice(0, DEFAULT_MAX_ARTIFACTS);
}

async function postWebhook({ webhookUrl, secret, text }) {
  if (!webhookUrl || !text) {
    return { code: 0, body: null, sent: false, reason: "missing" };
  }

  const response = await fetch(resolveWebhook(webhookUrl, secret), {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      msg_type: "text",
      content: {
        text,
      },
    }),
  });

  const bodyText = await response.text();
  let body = null;
  try {
    body = JSON.parse(bodyText);
  } catch (_err) {
    body = bodyText;
  }

  return {
    code: response.status,
    body,
    sent: response.ok,
    reason: response.ok ? "ok" : `HTTP ${response.status}`,
  };
}

function buildPackCommand(params) {
  const composeService = params.composeService || "android-builder";
  const extraArgs = Array.isArray(params.extraArgs) ? params.extraArgs : [];
  if (params.target !== "android" && params.target !== "ios") {
    throw new Error(`Unsupported target: ${params.target}`);
  }

  const baseArgs = [
    "scripts/package.sh",
    "--target",
    params.target,
    "--variant",
    params.variant,
    ...(extraArgs.length > 0 ? ["--", ...extraArgs] : []),
  ];

  if (params.target === "android" && params.useDocker) {
    return {
      command: "docker",
      args: [
        "compose",
        "run",
        "--rm",
        composeService,
        ...baseArgs,
      ],
    };
  }

  if (params.target === "android") {
    return { command: "bash", args: baseArgs };
  }

  if (params.useDocker) {
    throw new Error("Docker mode is currently supported for Android builds only.");
  }

  return { command: "bash", args: baseArgs };
}

function formatArtifactMessage(artifacts) {
  if (!artifacts || artifacts.length === 0) {
    return "No build artifacts found.";
  }

  return artifacts
    .map((artifact) => `- ${artifact.relativePath} (${artifact.sizeLabel}, ${artifact.modifiedAt})`)
    .join("\n");
}

function makeNotificationText({ target, variant, useDocker, collectArtifacts, message, artifacts }) {
  const lines = [
    "PackFlow 完成。",
    `- target: ${target}`,
    `- variant: ${variant}`,
    `- runner: ${useDocker && target === "android" ? "docker" : "local"}`,
  ];

  if (collectArtifacts && artifacts.length > 0) {
    lines.push("- 产物:");
    lines.push(formatArtifactMessage(artifacts));
  }

  if (message) {
    lines.push("", message);
  }

  return lines.join("\n");
}

function factory(pi) {
  const z = pi.zod;

  return [
    {
      name: "capno_packflow",
      label: "Capno PackFlow",
      description:
        "Run CapnoGraph packaging workflow (android/ios), collect outputs, and optionally notify a Feishu webhook.",
      parameters: z.object({
        target: z.enum(["android", "ios"]).default("android").describe("Build target."),
        variant: z.enum(["debug", "release"]).default("release").describe("Build variant."),
        useDocker: z.boolean().optional().default(true).describe("Use docker-compose for Android builds when available."),
        composeService: z.string().optional().default("android-builder").describe("Compose service name for docker mode."),
        extraArgs: z.array(z.string()).optional().default([]).describe("Extra args forwarded to scripts/package.sh after --."),
        run: z.boolean().optional().default(false).describe("Preview command when false. Execute when true."),
        collectArtifacts: z.boolean().optional().default(true).describe("Collect recent apk/aab/ipa outputs after execution."),
        includeChecksums: z.boolean().optional().default(false).describe("Compute sha256 for collected artifacts (slower for large files)."),
        notifyOnSuccess: z.boolean().optional().default(false).describe("Call Feishu webhook when build succeeds."),
        notifyWebhookUrl: z.string().optional().describe("Feishu outgoing webhook URL. If empty, env FEISHU_WEBHOOK_URL is used."),
        notifyWebhookSecret: z.string().optional().describe("Optional webhook secret for Feishu signed webhook mode."),
        notifyMessage: z.string().optional().describe("Optional extra message appended to Feishu notification."),
      }),

      async execute(_toolCallId, params, onUpdate, _ctx, signal) {
        const root = pi.cwd;
        const { command, args } = buildPackCommand(params);
        const commandText = [command, ...args].map((item) => (/[\s`]/.test(item) ? JSON.stringify(item) : item)).join(" ");

        if (!params.run) {
          return {
            content: [{
              type: "text",
              text: `Preview only. To execute, call with run=true.\n\n${commandText}`,
            }],
            details: {
              command: command,
              args,
              preview: true,
              runner: params.useDocker && params.target === "android" ? "docker" : "local",
            },
          };
        }

        onUpdate?.({
          content: [{ type: "text", text: `Running ${commandText}` }],
          details: { phase: "execute", command, args },
        });

        const result = await pi.exec(command, args, { cwd: root, signal });
        if (result.killed) {
          throw new Error("PackFlow was cancelled");
        }

        const output = [
          `Command: ${commandText}`,
          `Exit code: ${result.code}`,
          result.stdout ? `\nstdout:\n${result.stdout}` : "",
          result.stderr ? `\nstderr:\n${result.stderr}` : "",
        ]
          .filter(Boolean)
          .join("\n");

        if (result.code !== 0) {
          throw new Error(output);
        }

        const artifacts = params.collectArtifacts
          ? collectArtifacts(root, params.target, params.variant, params.includeChecksums)
          : [];

        let notification = null;
        if (params.notifyOnSuccess) {
          const webhook = params.notifyWebhookUrl || process.env.FEISHU_WEBHOOK_URL || null;
          if (webhook) {
            notification = await postWebhook({
              webhookUrl: webhook,
              secret: params.notifyWebhookSecret,
              text: makeNotificationText({
                target: params.target,
                variant: params.variant,
                useDocker: params.useDocker,
                collectArtifacts: params.collectArtifacts,
                message: params.notifyMessage,
                artifacts,
              }),
            });

            if (!notification.sent) {
              notification = {
                ...notification,
                reason: `Failed to post notification: ${notification.reason}`,
              };
            }
          } else {
            notification = {
              code: 0,
              body: null,
              sent: false,
              reason: "No webhook URL configured",
            };
          }
        }

        return {
          content: [
            { type: "text", text: output },
            ...(artifacts.length > 0 ? [{ type: "text", text: formatArtifactMessage(artifacts) }] : []),
          ],
          details: {
            command,
            args,
            code: result.code,
            artifacts,
            notification,
          },
        };
      },
    },
    {
      name: "capno_feishu_send",
      label: "Capno Feishu Send",
      description: "Send pack artifacts and a text message to Feishu via webhook.",
      parameters: z.object({
        title: z.string().optional().describe("Optional title text."),
        message: z.string().describe("Main text content to send."),
        artifactPaths: z.array(z.string()).optional().default([]).describe("Paths to include in the summary (not uploaded to Feishu)."),
        webhookUrl: z.string().optional().describe("Feishu incoming webhook URL."),
        webhookSecret: z.string().optional().describe("Optional webhook secret for signature mode."),
        includeStats: z.boolean().optional().default(true).describe("Include file size and modified time in the message."),
      }),

      async execute(_toolCallId, params, _onUpdate, _ctx, _signal) {
        const webhook = params.webhookUrl || process.env.FEISHU_WEBHOOK_URL;
        if (!webhook) {
          throw new Error("Missing webhookUrl and FEISHU_WEBHOOK_URL is not set.");
        }

        const root = pi.cwd;
        const artifactLines = [];
        for (const inputPath of params.artifactPaths || []) {
          const normalized = inputPath.startsWith("/") ? inputPath : path.resolve(root, inputPath);
          if (!fs.existsSync(normalized) || !fs.statSync(normalized).isFile()) {
            artifactLines.push(`- ${inputPath} (missing)`);
            continue;
          }

          const stat = fs.statSync(normalized);
          artifactLines.push(
            params.includeStats
              ? `- ${path.basename(normalized)} (${formatSize(stat.size)}, ${new Date(stat.mtimeMs).toISOString()})`
              : `- ${path.basename(normalized)}`,
          );
        }

        const titleLine = params.title ? `# ${params.title}\n` : "";
        const artifactLine = artifactLines.length > 0 ? `\n\n产物列表：\n${artifactLines.join("\n")}` : "";
        const text = `${titleLine}${params.message}${artifactLine}`;

        const webhookResult = await postWebhook({
          webhookUrl: webhook,
          secret: params.webhookSecret,
          text,
        });

        if (!webhookResult.sent) {
          throw new Error(`Feishu webhook send failed: ${webhookResult.reason}`);
        }

        return {
          content: [{
            type: "text",
            text: `Feishu webhook send successful.\nstatus=${webhookResult.code}`,
          }],
          details: { webhook: webhookResult },
        };
      },
    },
  ];
}

module.exports = factory;
module.exports.default = factory;
