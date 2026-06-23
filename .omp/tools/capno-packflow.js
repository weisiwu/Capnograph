const crypto = require("crypto");
const fs = require("fs");
const path = require("path");

const DEFAULT_MAX_ARTIFACTS = 20;
const DEFAULT_PACKFLOW_BASE_URL = "http://localhost:3001";
const DEFAULT_PACKFLOW_DB_PATH = "/Users/weisiwu_clawbot_mac/Desktop/work/github/packflow/data/packflow.db";
const DEFAULT_PACKFLOW_PROJECT_NAME = "CapnoGraph";
const DEFAULT_PACKFLOW_ANDROID_CONFIG_NAME = "Android Debug APK";
const DEFAULT_PACKFLOW_BRANCH = "monorepo_v1";
const FEISHU_FILE_UPLOAD_LIMIT_BYTES = 30 * 1024 * 1024;
const BUILD_ID_PATTERN = /[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}/i;

function resolveWebhook(baseUrl, secret) {
  if (!baseUrl) {
    return null;
  }

  if (!secret) {
    return baseUrl;
  }

  const timestamp = Math.floor(Date.now() / 1000).toString();
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

  const appCode =
    body && typeof body === "object"
      ? (body.StatusCode ?? body.code ?? null)
      : null;
  const sent = response.ok && (appCode === null || Number(appCode) === 0);

  return {
    code: response.status,
    body,
    sent,
    reason: sent ? "ok" : response.ok ? `Feishu code ${appCode}` : `HTTP ${response.status}`,
  };
}

function firstEnv(names) {
  for (const name of names) {
    if (process.env[name]) {
      return process.env[name];
    }
  }
  return null;
}

function normalizeBaseUrl(value) {
  return (value || DEFAULT_PACKFLOW_BASE_URL).replace(/\/+$/, "");
}

function resolvePackflowPublicBaseUrl(params) {
  return normalizeBaseUrl(
    params.packflowPublicBaseUrl ||
      process.env.PACKFLOW_PUBLIC_BASE_URL ||
      params.packflowBaseUrl,
  );
}

function makeArtifactDownloadUrl(artifact, params) {
  if (!artifact || !artifact.id) {
    return null;
  }

  return `${resolvePackflowPublicBaseUrl(params)}/api/artifacts/${encodeURIComponent(artifact.id)}/download`;
}

function annotateArtifactDelivery(artifacts, params) {
  if (params.includeArtifactDownloadLinks === false) {
    return artifacts || [];
  }

  return (artifacts || []).map((artifact) => ({
    ...artifact,
    downloadUrl: makeArtifactDownloadUrl(artifact, params),
    directFeishuFileUploadSupported:
      Number.isFinite(artifact.size_bytes) && artifact.size_bytes <= FEISHU_FILE_UPLOAD_LIMIT_BYTES,
  }));
}

function sqlString(value) {
  return String(value || "").replace(/'/g, "''");
}

async function fetchJson(url, options = {}) {
  const response = await fetch(url, options);
  const text = await response.text();
  let body = null;
  try {
    body = text ? JSON.parse(text) : null;
  } catch (_err) {
    body = text;
  }

  if (!response.ok) {
    const message = body && typeof body === "object" && body.error ? body.error : text;
    throw new Error(`HTTP ${response.status}: ${message || response.statusText}`);
  }

  return { response, body };
}

async function resolvePackflowAuthHeaders(params) {
  const token = params.packflowToken || process.env.PACKFLOW_AGENT_TOKEN;
  if (token) {
    return { Authorization: `Bearer ${token}` };
  }

  const baseUrl = normalizeBaseUrl(params.packflowBaseUrl);
  const username = params.packflowUsername || process.env.PACKFLOW_USERNAME || "admin";
  const password =
    params.packflowPassword ||
    process.env.PACKFLOW_PASSWORD ||
    process.env.PACKFLOW_ADMIN_PASSWORD ||
    "packflow-admin";

  const response = await fetch(`${baseUrl}/api/auth/login`, {
    method: "POST",
    redirect: "manual",
    headers: {
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: new URLSearchParams({ username, password }).toString(),
  });

  if (!response.ok && (response.status < 300 || response.status >= 400)) {
    throw new Error(`Packflow login failed: HTTP ${response.status}`);
  }

  const setCookie = response.headers.get("set-cookie") || "";
  const sessionMatch = setCookie.match(/packflow_session=[^;]+/);
  if (!sessionMatch) {
    throw new Error("Packflow login did not return a session cookie. Set PACKFLOW_AGENT_TOKEN instead.");
  }

  return { Cookie: sessionMatch[0] };
}

async function startPackflowBuild(params) {
  const baseUrl = normalizeBaseUrl(params.packflowBaseUrl);
  const authHeaders = await resolvePackflowAuthHeaders(params);
  const { body } = await fetchJson(`${baseUrl}/api/agent/tools/start-build`, {
    method: "POST",
    headers: {
      ...authHeaders,
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      projectId: params.projectId || undefined,
      projectName: params.projectName || DEFAULT_PACKFLOW_PROJECT_NAME,
      configName: params.configName || DEFAULT_PACKFLOW_ANDROID_CONFIG_NAME,
      branch: params.branch || DEFAULT_PACKFLOW_BRANCH,
    }),
  });

  return body;
}

async function sqliteJson(pi, dbPath, sql, cwd, signal) {
  const result = await pi.exec("sqlite3", ["-json", dbPath, sql], { cwd, signal });
  if (result.killed) {
    throw new Error("Packflow sqlite query was cancelled.");
  }
  if (result.code !== 0) {
    throw new Error(`sqlite3 failed: ${result.stderr || result.stdout}`);
  }
  return JSON.parse(result.stdout || "[]");
}

async function readPackflowBuild(pi, dbPath, buildId, cwd, signal) {
  const rows = await sqliteJson(
    pi,
    dbPath,
    `select id, project_id, branch, build_number, version, status, trigger, commit_sha, commit_message, started_at, finished_at, duration_ms, log_path, error_summary, artifact_count, created_at, updated_at from builds where id='${sqlString(buildId)}';`,
    cwd,
    signal,
  );
  return rows[0] || null;
}

async function readPackflowArtifacts(pi, dbPath, buildId, cwd, signal) {
  return sqliteJson(
    pi,
    dbPath,
    `select id, build_id, project_id, version, file_name, storage_path, size_bytes, sha256, download_count, created_at from artifacts where build_id='${sqlString(buildId)}' order by created_at desc;`,
    cwd,
    signal,
  );
}

function resolvePackflowDbPath(params) {
  return params.packflowDbPath || process.env.PACKFLOW_DB_PATH || DEFAULT_PACKFLOW_DB_PATH;
}

async function readPackflowProjects(pi, params, cwd, signal) {
  const dbPath = resolvePackflowDbPath(params);
  const projectName = params.projectName || DEFAULT_PACKFLOW_PROJECT_NAME;
  return sqliteJson(
    pi,
    dbPath,
    `select id, project_name, name, platform, branch, enabled, workdir, install_command, build_command, artifact_path, updated_at from projects where project_name='${sqlString(projectName)}' order by platform, name;`,
    cwd,
    signal,
  );
}

async function readPackflowBuildHistory(pi, params, cwd, signal) {
  const dbPath = resolvePackflowDbPath(params);
  const projectName = params.projectName || DEFAULT_PACKFLOW_PROJECT_NAME;
  const limit = Math.min(Math.max(Number(params.limit || 10), 1), 50);
  const where = [`p.project_name='${sqlString(projectName)}'`];

  if (params.configName) {
    where.push(`p.name='${sqlString(params.configName)}'`);
  }

  if (params.status) {
    where.push(`b.status='${sqlString(params.status)}'`);
  }

  return sqliteJson(
    pi,
    dbPath,
    `select b.id, b.build_number, b.status, b.branch, b.commit_sha, b.started_at, b.finished_at, b.duration_ms, b.error_summary, b.artifact_count, p.project_name, p.name as config_name, p.platform from builds b join projects p on p.id=b.project_id where ${where.join(" and ")} order by b.created_at desc limit ${limit};`,
    cwd,
    signal,
  );
}

async function readPackflowBuildDetail(pi, params, cwd, signal) {
  const dbPath = resolvePackflowDbPath(params);
  const buildId = params.buildId || "";
  const rows = await sqliteJson(
    pi,
    dbPath,
    `select b.id, b.build_number, b.status, b.branch, b.commit_sha, b.commit_message, b.started_at, b.finished_at, b.duration_ms, b.log_path, b.error_summary, b.artifact_count, p.project_name, p.name as config_name, p.platform, p.workdir, p.build_command, p.artifact_path from builds b join projects p on p.id=b.project_id where b.id='${sqlString(buildId)}';`,
    cwd,
    signal,
  );
  const build = rows[0] || null;
  const artifacts = build ? await readPackflowArtifacts(pi, dbPath, build.id, cwd, signal) : [];
  return { build, artifacts };
}

async function waitForPackflowBuild(pi, params, buildId, onUpdate, cwd, signal) {
  const dbPath = resolvePackflowDbPath(params);
  const timeoutSeconds = Math.max(1, Number(params.timeoutSeconds || 180));
  const pollIntervalSeconds = Math.max(1, Number(params.pollIntervalSeconds || 3));
  const startedAt = Date.now();
  let lastStatus = null;

  while (Date.now() - startedAt <= timeoutSeconds * 1000) {
    const build = await readPackflowBuild(pi, dbPath, buildId, cwd, signal);
    if (!build) {
      throw new Error(`Packflow build not found in DB: ${buildId}`);
    }

    if (build.status !== lastStatus) {
      lastStatus = build.status;
      onUpdate?.({
        content: [{ type: "text", text: `Packflow build ${buildId} status=${build.status}` }],
        details: { phase: "packflow-status", build },
      });
    }

    if (["success", "failed", "canceled"].includes(build.status)) {
      const artifacts = await readPackflowArtifacts(pi, dbPath, buildId, cwd, signal);
      return { build, artifacts };
    }

    await new Promise((resolve) => setTimeout(resolve, pollIntervalSeconds * 1000));
  }

  const build = await readPackflowBuild(pi, dbPath, buildId, cwd, signal);
  throw new Error(`Timed out waiting for Packflow build ${buildId}. Last status=${build ? build.status : "unknown"}`);
}

function makePackflowAgentText({ build, artifacts, project, links, note }) {
  const lines = [
    "CapnoGraph OMP Bot 打包结果",
    `- config: ${project && project.configName ? project.configName : DEFAULT_PACKFLOW_ANDROID_CONFIG_NAME}`,
    `- status: ${build.status}`,
    `- build: ${build.id}`,
    `- branch: ${build.branch}`,
  ];

  if (Number.isFinite(build.duration_ms) && build.duration_ms > 0) {
    lines.push(`- duration: ${(build.duration_ms / 1000).toFixed(3)}s`);
  }

  if (build.commit_sha) {
    lines.push(`- commit: ${build.commit_sha.slice(0, 12)}`);
  }

  if (build.error_summary) {
    lines.push(`- error: ${build.error_summary}`);
  }

  if (artifacts && artifacts.length > 0) {
    lines.push("- artifacts:");
    for (const artifact of artifacts) {
      lines.push(`  - ${artifact.file_name} (${formatSize(artifact.size_bytes)})`);
      if (artifact.sha256) {
        lines.push(`    sha256: ${artifact.sha256}`);
      }
      if (artifact.downloadUrl) {
        lines.push(`    download: ${artifact.downloadUrl}`);
      }
      if (artifact.directFeishuFileUploadSupported === false) {
        lines.push("    delivery: 文件超过飞书文件上传上限，已改为发送 Packflow 下载链接");
      }
    }
  } else {
    lines.push("- artifacts: none");
  }

  if (links && links.build) {
    lines.push(`- build URL: ${links.build}`);
  }

  if (note) {
    lines.push("", note);
  }

  return lines.join("\n");
}

async function runPackflowAgent(pi, params, onUpdate, root, signal) {
  const started = await startPackflowBuild(params);
  const buildId = started.build && started.build.id;
  if (!buildId) {
    throw new Error(`Packflow did not return a build id: ${JSON.stringify(started)}`);
  }

  onUpdate?.({
    content: [{ type: "text", text: `Packflow build accepted: ${buildId}` }],
    details: { phase: "packflow-started", response: started },
  });

  let finalBuild = started.build;
  let artifacts = [];
  if (params.waitForCompletion !== false) {
    const completed = await waitForPackflowBuild(pi, params, buildId, onUpdate, root, signal);
    finalBuild = completed.build;
    artifacts = completed.artifacts;
  }
  const artifactsForMessage = annotateArtifactDelivery(artifacts, params);

  const shouldNotify =
    params.notifyFeishu &&
    (finalBuild.status === "success" || params.notifyOnFailure !== false);
  let notification = null;
  if (shouldNotify) {
    const webhook =
      params.feishuWebhookUrl ||
      firstEnv(["CAPNOGRAPH_OMP_BOT_WEBHOOK_URL", "FEISHU_WEBHOOK_URL", "FEISHU_WEBHOOK"]);
    if (webhook) {
      notification = await postWebhook({
        webhookUrl: webhook,
        secret:
          params.feishuWebhookSecret ||
          firstEnv(["CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET", "FEISHU_WEBHOOK_SECRET"]),
        text: makePackflowAgentText({
          build: finalBuild,
          artifacts: artifactsForMessage,
          project: started.project,
          links: started.links,
          note: params.notifyMessage,
        }),
      });
    } else {
      notification = {
        code: 0,
        body: null,
        sent: false,
        reason: "No Feishu webhook configured. Set CAPNOGRAPH_OMP_BOT_WEBHOOK_URL, FEISHU_WEBHOOK_URL, or FEISHU_WEBHOOK.",
      };
    }
  }

  const summary = makePackflowAgentText({
    build: finalBuild,
    artifacts: artifactsForMessage,
    project: started.project,
    links: started.links,
    note: notification
      ? `Feishu notification: ${notification.sent ? "sent" : `not sent (${notification.reason})`}`
      : null,
  });

  if (params.waitForCompletion !== false && finalBuild.status !== "success") {
    throw new Error(summary);
  }

  return {
    content: [{ type: "text", text: summary }],
    details: {
      started,
      build: finalBuild,
      artifacts: artifactsForMessage,
      notification,
    },
  };
}

function formatDuration(ms) {
  if (!Number.isFinite(ms) || ms <= 0) {
    return "n/a";
  }
  return `${(ms / 1000).toFixed(3)}s`;
}

function shortSha(value) {
  return value ? String(value).slice(0, 12) : "n/a";
}

function formatPackflowProjects(projects) {
  if (!projects.length) {
    return `未找到 Packflow 项目：${DEFAULT_PACKFLOW_PROJECT_NAME}`;
  }

  const lines = [`Packflow ${projects[0].project_name} 配置`];
  for (const project of projects) {
    lines.push(
      `- ${project.name} [${project.platform}] ${project.enabled ? "enabled" : "disabled"}`,
      `  branch: ${project.branch}`,
      `  build: ${project.build_command}`,
      `  artifacts: ${project.artifact_path}`,
    );
  }
  return lines.join("\n");
}

function formatPackflowHistory(rows, title) {
  if (!rows.length) {
    return `未找到 Packflow 构建记录：${DEFAULT_PACKFLOW_PROJECT_NAME}`;
  }

  const lines = [title || `Packflow ${rows[0].project_name} 构建历史`];
  for (const row of rows) {
    lines.push(
      `- #${row.build_number} ${row.config_name} ${row.status} ${formatDuration(row.duration_ms)} artifacts=${row.artifact_count}`,
      `  build: ${row.id}`,
      `  branch: ${row.branch} commit: ${shortSha(row.commit_sha)} finished: ${row.finished_at || row.started_at || "n/a"}`,
    );
    if (row.error_summary) {
      lines.push(`  error: ${row.error_summary}`);
    }
  }
  return lines.join("\n");
}

function formatPackflowDetail({ build, artifacts }) {
  if (!build) {
    return "未找到指定 Packflow build。";
  }

  const lines = [
    `Packflow build 详情`,
    `- build: ${build.id}`,
    `- config: ${build.config_name}`,
    `- status: ${build.status}`,
    `- branch: ${build.branch}`,
    `- duration: ${formatDuration(build.duration_ms)}`,
    `- commit: ${shortSha(build.commit_sha)}`,
    `- artifacts: ${build.artifact_count}`,
  ];

  if (build.error_summary) {
    lines.push(`- error: ${build.error_summary}`);
  }

  if (artifacts.length > 0) {
    lines.push("- artifact files:");
    for (const artifact of artifacts) {
      lines.push(`  - ${artifact.file_name} (${formatSize(artifact.size_bytes)})`);
      lines.push(`    sha256: ${artifact.sha256}`);
      lines.push(`    storage: ${artifact.storage_path}`);
    }
  }

  return lines.join("\n");
}

function makePackflowCommandHelp() {
  return [
    "CapnoGraph OMP Bot 可用口令",
    "- 打包 / 打 APK / 打 Android 包 / capno_packflow_agent：触发 Android Debug APK 打包",
    "- 打包状态 / 最新打包 / packflow latest：查看最新一条构建",
    "- 打包历史 / 最近 10 条打包 / packflow history：查看构建历史",
    "- 打包详情 <buildId> / packflow build <buildId>：查看指定 build 的产物和错误",
    "- 打包配置 / packflow info：查看 CapnoGraph 在 Packflow 下的配置",
    "- 打包帮助 / packflow help：查看本帮助",
    "",
    "默认打包配置：CapnoGraph / Android Debug APK / monorepo_v1。",
    "当前不建议用机器人触发 Android Release APK；release 仍需修复 R8/minify。",
  ].join("\n");
}

function parsePackflowBotCommand(text) {
  const raw = String(text || "").trim();
  const normalized = raw
    .replace(/@\S+/g, " ")
    .replace(/\s+/g, " ")
    .trim()
    .toLowerCase();
  const buildId = raw.match(BUILD_ID_PATTERN)?.[0] || null;
  const limitMatch =
    raw.match(/(?:最近|last|limit)\s*(\d{1,2})/i) ||
    raw.match(/(\d{1,2})\s*(?:条|个|records?)/i);
  const limit = limitMatch ? Math.min(Math.max(Number(limitMatch[1]), 1), 50) : 10;

  if (!normalized || /帮助|help|口令|命令/.test(normalized)) {
    return { action: "help", raw, limit };
  }

  if (buildId) {
    return { action: "detail", raw, buildId, limit };
  }

  if (
    /历史|记录|history|records|list/.test(normalized) ||
    /最近\s*\d|last\s*\d|limit\s*\d|\d{1,2}\s*(条|个|records?)/i.test(normalized)
  ) {
    return { action: "history", raw, limit };
  }

  if (/状态|最新|latest|last|status/.test(normalized)) {
    return { action: "latest", raw, limit: 1 };
  }

  if (/配置|信息|项目|configs?|projects?|info/.test(normalized)) {
    return { action: "projects", raw, limit };
  }

  if (/release|发布包|正式包/.test(normalized)) {
    return { action: "release-blocked", raw, limit };
  }

  if (
    /capno_packflow_agent|打包|打 apk|打apk|android 包|安卓包|构建|build|apk/.test(normalized)
  ) {
    return { action: "build", raw, limit };
  }

  return { action: "unknown", raw, limit };
}

async function executePackflowQuery(pi, params, root, signal) {
  const action = params.action || "history";

  if (action === "help") {
    return {
      content: [{ type: "text", text: makePackflowCommandHelp() }],
      details: { action },
    };
  }

  if (action === "projects") {
    const projects = await readPackflowProjects(pi, params, root, signal);
    return {
      content: [{ type: "text", text: formatPackflowProjects(projects) }],
      details: { action, projects },
    };
  }

  if (action === "detail") {
    if (!params.buildId) {
      throw new Error("Missing buildId for Packflow detail query.");
    }
    const detail = await readPackflowBuildDetail(pi, params, root, signal);
    return {
      content: [{ type: "text", text: formatPackflowDetail(detail) }],
      details: { action, ...detail },
    };
  }

  const rows = await readPackflowBuildHistory(
    pi,
    {
      ...params,
      limit: action === "latest" ? 1 : params.limit,
    },
    root,
    signal,
  );

  return {
    content: [{
      type: "text",
      text: formatPackflowHistory(
        rows,
        action === "latest" && rows[0] ? `Packflow ${rows[0].project_name} 最新构建` : null,
      ),
    }],
    details: { action, builds: rows },
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
        notifyWebhookUrl: z.string().optional().describe("Feishu outgoing webhook URL. Env CAPNOGRAPH_OMP_BOT_WEBHOOK_URL, FEISHU_WEBHOOK_URL, or FEISHU_WEBHOOK is also supported."),
        notifyWebhookSecret: z.string().optional().describe("Optional webhook secret for Feishu signed webhook mode. Env CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET or FEISHU_WEBHOOK_SECRET is also supported."),
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
          const webhook =
            params.notifyWebhookUrl ||
            firstEnv(["CAPNOGRAPH_OMP_BOT_WEBHOOK_URL", "FEISHU_WEBHOOK_URL", "FEISHU_WEBHOOK"]);
          if (webhook) {
            notification = await postWebhook({
              webhookUrl: webhook,
              secret:
                params.notifyWebhookSecret ||
                firstEnv(["CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET", "FEISHU_WEBHOOK_SECRET"]),
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
              reason: "No webhook URL configured. Set CAPNOGRAPH_OMP_BOT_WEBHOOK_URL, FEISHU_WEBHOOK_URL, or FEISHU_WEBHOOK.",
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
      name: "capno_packflow_agent",
      label: "Capno Packflow Agent",
      description:
        "Trigger the local Packflow backend as an AI-callable build capability, wait for completion, collect Packflow artifacts, and optionally notify CapnoGraph OMP Bot in Feishu.",
      parameters: z.object({
        projectId: z.string().optional().describe("Packflow project/config ID. Most precise when provided."),
        projectName: z.string().optional().default(DEFAULT_PACKFLOW_PROJECT_NAME).describe("Packflow project name."),
        configName: z.string().optional().default(DEFAULT_PACKFLOW_ANDROID_CONFIG_NAME).describe("Packflow config name, default is the fast Android Debug APK path."),
        branch: z.string().optional().default(DEFAULT_PACKFLOW_BRANCH).describe("Git branch to build."),
        packflowBaseUrl: z.string().optional().default(DEFAULT_PACKFLOW_BASE_URL).describe("Packflow backend base URL."),
        packflowPublicBaseUrl: z.string().optional().describe("Public Packflow base URL used in Feishu artifact download links. Env PACKFLOW_PUBLIC_BASE_URL is also supported."),
        packflowDbPath: z.string().optional().default(DEFAULT_PACKFLOW_DB_PATH).describe("Local Packflow SQLite DB path used for status polling."),
        packflowToken: z.string().optional().describe("Optional Packflow agent bearer token. Env PACKFLOW_AGENT_TOKEN is also supported."),
        packflowUsername: z.string().optional().describe("Fallback web login username when no agent token is configured."),
        packflowPassword: z.string().optional().describe("Fallback web login password when no agent token is configured."),
        waitForCompletion: z.boolean().optional().default(true).describe("Poll local Packflow DB until build finishes."),
        timeoutSeconds: z.number().optional().default(180).describe("Maximum wait time when waitForCompletion is true."),
        pollIntervalSeconds: z.number().optional().default(3).describe("Polling interval in seconds."),
        notifyFeishu: z.boolean().optional().default(false).describe("Send a text notification to CapnoGraph OMP Bot via Feishu webhook."),
        includeArtifactDownloadLinks: z.boolean().optional().default(true).describe("Include Packflow artifact download links in returned output and Feishu notifications."),
        feishuWebhookUrl: z.string().optional().describe("Feishu incoming webhook URL. Env CAPNOGRAPH_OMP_BOT_WEBHOOK_URL, FEISHU_WEBHOOK_URL, or FEISHU_WEBHOOK is also supported."),
        feishuWebhookSecret: z.string().optional().describe("Optional Feishu webhook secret. Env CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET or FEISHU_WEBHOOK_SECRET is also supported."),
        notifyOnFailure: z.boolean().optional().default(true).describe("When notifyFeishu is true, also send failed/canceled build notifications."),
        notifyMessage: z.string().optional().describe("Extra text appended to the Feishu notification."),
      }),

      async execute(_toolCallId, params, onUpdate, _ctx, signal) {
        const root = pi.cwd;
        return runPackflowAgent(pi, params, onUpdate, root, signal);
      },
    },
    {
      name: "capno_packflow_query",
      label: "Capno Packflow Query",
      description:
        "Query CapnoGraph Packflow project configs, latest build, build history, or build detail from the local Packflow SQLite database.",
      parameters: z.object({
        action: z.enum(["help", "projects", "latest", "history", "detail"]).optional().default("history").describe("Packflow query action."),
        projectName: z.string().optional().default(DEFAULT_PACKFLOW_PROJECT_NAME).describe("Packflow project name."),
        configName: z.string().optional().describe("Optional Packflow config name filter."),
        status: z.string().optional().describe("Optional build status filter, such as success or failed."),
        buildId: z.string().optional().describe("Build ID for detail queries."),
        limit: z.number().optional().default(10).describe("Maximum history rows, 1-50."),
        packflowDbPath: z.string().optional().default(DEFAULT_PACKFLOW_DB_PATH).describe("Local Packflow SQLite DB path."),
      }),

      async execute(_toolCallId, params, _onUpdate, _ctx, signal) {
        return executePackflowQuery(pi, params, pi.cwd, signal);
      },
    },
    {
      name: "capno_packflow_bot_command",
      label: "Capno Packflow Bot Command",
      description:
        "Parse a Feishu message text command for CapnoGraph OMP Bot and route it to Packflow build, status, history, detail, config, or help actions.",
      parameters: z.object({
        text: z.string().describe("Raw user message text from Feishu."),
        dryRun: z.boolean().optional().default(false).describe("Return the mapped action without starting a build."),
        projectName: z.string().optional().default(DEFAULT_PACKFLOW_PROJECT_NAME).describe("Packflow project name."),
        configName: z.string().optional().default(DEFAULT_PACKFLOW_ANDROID_CONFIG_NAME).describe("Packflow config name for build commands."),
        branch: z.string().optional().default(DEFAULT_PACKFLOW_BRANCH).describe("Git branch to build."),
        packflowBaseUrl: z.string().optional().default(DEFAULT_PACKFLOW_BASE_URL).describe("Packflow backend base URL."),
        packflowPublicBaseUrl: z.string().optional().describe("Public Packflow base URL used in Feishu artifact download links. Env PACKFLOW_PUBLIC_BASE_URL is also supported."),
        packflowDbPath: z.string().optional().default(DEFAULT_PACKFLOW_DB_PATH).describe("Local Packflow SQLite DB path."),
        packflowToken: z.string().optional().describe("Optional Packflow agent bearer token. Env PACKFLOW_AGENT_TOKEN is also supported."),
        packflowUsername: z.string().optional().describe("Fallback web login username when no agent token is configured."),
        packflowPassword: z.string().optional().describe("Fallback web login password when no agent token is configured."),
        waitForCompletion: z.boolean().optional().default(true).describe("Poll local Packflow DB until build finishes."),
        timeoutSeconds: z.number().optional().default(180).describe("Maximum wait time for build commands."),
        pollIntervalSeconds: z.number().optional().default(3).describe("Polling interval in seconds."),
        notifyFeishu: z.boolean().optional().default(true).describe("Send build result to CapnoGraph OMP Bot for build commands."),
        includeArtifactDownloadLinks: z.boolean().optional().default(true).describe("Include Packflow artifact download links in returned output and Feishu notifications."),
        feishuWebhookUrl: z.string().optional().describe("Feishu incoming webhook URL. Env CAPNOGRAPH_OMP_BOT_WEBHOOK_URL, FEISHU_WEBHOOK_URL, or FEISHU_WEBHOOK is also supported."),
        feishuWebhookSecret: z.string().optional().describe("Optional Feishu webhook secret."),
        limit: z.number().optional().default(10).describe("Maximum rows for history commands."),
      }),

      async execute(_toolCallId, params, onUpdate, _ctx, signal) {
        const root = pi.cwd;
        const parsed = parsePackflowBotCommand(params.text);

        if (parsed.action === "help") {
          return {
            content: [{ type: "text", text: makePackflowCommandHelp() }],
            details: { parsed },
          };
        }

        if (parsed.action === "unknown") {
          return {
            content: [{
              type: "text",
              text: `未识别口令：${parsed.raw || "(empty)"}\n\n${makePackflowCommandHelp()}`,
            }],
            details: { parsed },
          };
        }

        if (parsed.action === "release-blocked") {
          return {
            content: [{
              type: "text",
              text: [
                "已识别为 release 打包请求，但当前不建议通过机器人触发。",
                "原因：Android Release APK 仍在 R8/minify 阶段不稳定。",
                "请使用「打包」或「打 APK」触发 Android Debug APK 快速包；release 修复后再开放机器人口令。",
              ].join("\n"),
            }],
            details: { parsed },
          };
        }

        if (parsed.action === "build") {
          const mappedParams = {
            projectName: params.projectName || DEFAULT_PACKFLOW_PROJECT_NAME,
            configName: params.configName || DEFAULT_PACKFLOW_ANDROID_CONFIG_NAME,
            branch: params.branch || DEFAULT_PACKFLOW_BRANCH,
            packflowBaseUrl: params.packflowBaseUrl,
            packflowPublicBaseUrl: params.packflowPublicBaseUrl,
            packflowDbPath: params.packflowDbPath,
            packflowToken: params.packflowToken,
            packflowUsername: params.packflowUsername,
            packflowPassword: params.packflowPassword,
            waitForCompletion: params.waitForCompletion !== false,
            timeoutSeconds: params.timeoutSeconds || 180,
            pollIntervalSeconds: params.pollIntervalSeconds || 3,
            notifyFeishu: params.notifyFeishu !== false,
            includeArtifactDownloadLinks: params.includeArtifactDownloadLinks !== false,
            feishuWebhookUrl: params.feishuWebhookUrl,
            feishuWebhookSecret: params.feishuWebhookSecret,
            notifyOnFailure: true,
            notifyMessage: `飞书口令：${parsed.raw}`,
          };

          if (params.dryRun) {
            return {
              content: [{
                type: "text",
                text: [
                  "已映射为 Packflow 打包命令，但 dryRun=true，未触发构建。",
                  `tool: capno_packflow_agent`,
                  `projectName: ${mappedParams.projectName}`,
                  `configName: ${mappedParams.configName}`,
                  `branch: ${mappedParams.branch}`,
                ].join("\n"),
              }],
              details: { parsed, mappedTool: "capno_packflow_agent", mappedParams },
            };
          }

          const result = await runPackflowAgent(pi, mappedParams, onUpdate, root, signal);
          return {
            ...result,
            details: {
              parsed,
              mappedTool: "capno_packflow_agent",
              ...result.details,
            },
          };
        }

        const queryParams = {
          action: parsed.action,
          buildId: parsed.buildId,
          projectName: params.projectName || DEFAULT_PACKFLOW_PROJECT_NAME,
          limit: params.limit || parsed.limit,
          packflowDbPath: params.packflowDbPath,
        };

        const result = await executePackflowQuery(pi, queryParams, root, signal);
        return {
          ...result,
          details: {
            parsed,
            mappedTool: "capno_packflow_query",
            ...result.details,
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
        const webhook =
          params.webhookUrl ||
          firstEnv(["CAPNOGRAPH_OMP_BOT_WEBHOOK_URL", "FEISHU_WEBHOOK_URL", "FEISHU_WEBHOOK"]);
        if (!webhook) {
          throw new Error("Missing webhookUrl. Set CAPNOGRAPH_OMP_BOT_WEBHOOK_URL, FEISHU_WEBHOOK_URL, or FEISHU_WEBHOOK.");
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
          secret:
            params.webhookSecret ||
            firstEnv(["CAPNOGRAPH_OMP_BOT_WEBHOOK_SECRET", "FEISHU_WEBHOOK_SECRET"]),
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
