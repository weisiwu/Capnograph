const fs = require("fs");
const path = require("path");

function cleanCell(value) {
  return String(value || "")
    .replace(/<br\s*\/?>/gi, ",")
    .replace(/`/g, "")
    .trim();
}

function splitCellValues(value) {
  return cleanCell(value)
    .split(/[;；,，、]/)
    .map((item) => item.trim())
    .filter(Boolean);
}

function unique(items) {
  return Array.from(new Set(items));
}

function pathExists(root, relPath) {
  if (!relPath || path.isAbsolute(relPath)) {
    return false;
  }
  return fs.existsSync(path.join(root, relPath));
}

function normalizeProjectPath(root, rawPath) {
  let value = cleanCell(rawPath)
    .replace(/^['"]|['"]$/g, "")
    .replace(/:\d+(?::\d+)?$/, "")
    .trim();

  if (!value || value === "n/a" || value === "-") {
    return null;
  }

  if (value.startsWith("http://") || value.startsWith("https://")) {
    return { input: value, candidates: [value] };
  }

  if (value.startsWith("./")) {
    value = value.slice(2);
  }

  const candidates = [value];

  const oldAndroidRootNames = new Set([
    "app",
    "hotmeltprint",
    "gradle",
  ]);
  const firstSegment = value.split("/")[0];
  if (
    oldAndroidRootNames.has(firstSegment) ||
    [
      "settings.gradle.kts",
      "build.gradle.kts",
      "gradle.properties",
      "data_version_list.txt",
      "JDK_SETUP.md",
    ].includes(value)
  ) {
    candidates.unshift(`apps/android/${value}`);
  }

  if (
    !value.startsWith("apps/") &&
    !value.startsWith("context/") &&
    !value.startsWith("scripts/") &&
    !value.startsWith("docker/") &&
    !value.startsWith(".")
  ) {
    candidates.push(`apps/android/${value}`, `apps/ios/${value}`);
  }

  return {
    input: value,
    candidates: unique(candidates).map((candidate) => ({
      path: candidate,
      exists: candidate.startsWith("http") ? null : pathExists(root, candidate),
    })),
  };
}

function parseRgLine(root, line) {
  const match = line.match(/^(.+?):(\d+):(.*)$/);
  if (!match) {
    return { raw: line };
  }

  const [, file, lineNumber, content] = match;
  const row = {
    file,
    line: Number(lineNumber),
    text: content.trim(),
  };

  if (!content.includes("|")) {
    return row;
  }

  const cells = content
    .split("|")
    .map((cell) => cell.trim())
    .filter(Boolean);

  if (cells.length < 5 || cells[0] === "---" || cells[0] === "实体") {
    return row;
  }

  let sourceCell = "";
  let contextCell = "";
  if (file.endsWith("entity-id-mapping.md")) {
    row.entity = cleanCell(cells[0]);
    row.id = cleanCell(cells[1]);
    row.domain = cleanCell(cells[2]);
    sourceCell = cells[3];
    contextCell = cells[4];
  } else if (file.endsWith("实体标识映射.md")) {
    row.entity = cleanCell(cells[0]);
    row.id = cleanCell(cells[1]);
    row.domain = cleanCell(cells[3]);
    sourceCell = cells[4];
    contextCell = cells[5];
  }

  const sources = splitCellValues(sourceCell)
    .map((item) => normalizeProjectPath(root, item))
    .filter(Boolean);
  const contexts = splitCellValues(contextCell)
    .map((item) => normalizeProjectPath(root, item))
    .filter(Boolean);

  if (sources.length > 0) {
    row.sources = sources;
  }
  if (contexts.length > 0) {
    row.contexts = contexts;
  }

  return row;
}

function formatLookupResult(query, rows) {
  if (rows.length === 0) {
    return `No CapnoGraph context matches found for "${query}". Try a class name, Chinese display name, page constant, protocol term, resource name, or file path.`;
  }

  const parts = [`Found ${rows.length} CapnoGraph context match(es) for "${query}":`];
  for (const row of rows) {
    parts.push(`\n- ${row.file}:${row.line}`);
    if (row.entity || row.id || row.domain) {
      parts.push(`  entity: ${[row.entity, row.id, row.domain].filter(Boolean).join(" | ")}`);
    }
    parts.push(`  text: ${row.text}`);
    if (row.sources) {
      const sourceText = row.sources
        .flatMap((source) => source.candidates)
        .map((candidate) => `${candidate.exists === false ? "missing:" : ""}${candidate.path}`)
        .join(", ");
      parts.push(`  source candidates: ${sourceText}`);
    }
    if (row.contexts) {
      const contextText = row.contexts
        .flatMap((context) => context.candidates)
        .map((candidate) => `${candidate.exists === false ? "missing:" : ""}${candidate.path}`)
        .join(", ");
      parts.push(`  context candidates: ${contextText}`);
    }
  }
  return parts.join("\n");
}

function factory(pi) {
  const z = pi.zod;

  return [
    {
      name: "capno_context_lookup",
      label: "Capno Context Lookup",
      description:
        "Search CapnoGraph context locator files for an entity, ID, class, Chinese label, resource, workflow, or file path and return source/context candidates with monorepo path normalization.",
      parameters: z.object({
        query: z.string().min(1).describe("Entity, class, page, ID, Chinese label, resource, protocol term, or path to look up."),
        maxMatches: z.number().int().min(1).max(80).optional().default(20),
        includeDocs: z.boolean().optional().default(true).describe("Also search context/docs and generated entity docs."),
      }),

      async execute(_toolCallId, params, onUpdate, _ctx, signal) {
        const root = pi.cwd;
        const searchPaths = [
          "context/entity-id-mapping.md",
          "context/实体标识映射.md",
          "context/项目上下文.md",
          "context/项目记忆.md",
          "context/文档/代码库上下文.md",
          "README.md",
          "AGENTS.md",
        ];
        if (params.includeDocs) {
          searchPaths.push("context/docs", "context/文档/实体");
        }

        const existingPaths = searchPaths.filter((item) => fs.existsSync(path.join(root, item)));
        onUpdate?.({
          content: [{ type: "text", text: `Searching ${existingPaths.length} CapnoGraph context location(s)...` }],
          details: { phase: "search", query: params.query },
        });

        const result = await pi.exec(
          "rg",
          [
            "-n",
            "--ignore-case",
            "--fixed-strings",
            "--",
            params.query,
            ...existingPaths,
          ],
          { cwd: root, signal },
        );

        if (result.killed) {
          throw new Error("Context lookup was cancelled");
        }
        if (result.code > 1) {
          throw new Error(result.stderr || "rg failed while searching CapnoGraph context");
        }

        const rows = result.stdout
          .split("\n")
          .filter(Boolean)
          .slice(0, params.maxMatches)
          .map((line) => parseRgLine(root, line));

        return {
          content: [{ type: "text", text: formatLookupResult(params.query, rows) }],
          details: { query: params.query, matches: rows },
        };
      },
    },
    {
      name: "capno_package",
      label: "Capno Package",
      description:
        "Preview or run the CapnoGraph root packaging wrapper for Android or iOS. Defaults to preview mode; set run=true to execute.",
      parameters: z.object({
        target: z.enum(["android", "ios"]).describe("Platform target to package."),
        variant: z.enum(["debug", "release"]).optional().default("debug").describe("Build variant."),
        extraArgs: z.array(z.string()).optional().default([]).describe("Arguments appended after --, for Gradle or xcodebuild/export options."),
        run: z.boolean().optional().default(false).describe("If false, only preview the command. If true, execute it."),
      }),

      async execute(_toolCallId, params, onUpdate, _ctx, signal) {
        const root = pi.cwd;
        const args = ["scripts/package.sh", "--target", params.target, "--variant", params.variant];
        if (params.extraArgs.length > 0) {
          args.push("--", ...params.extraArgs);
        }

        const commandText = ["bash", ...args].map((item) => (/\s/.test(item) ? JSON.stringify(item) : item)).join(" ");
        if (!params.run) {
          return {
            content: [{ type: "text", text: `Preview only. To execute, call with run=true.\n\n${commandText}` }],
            details: { command: "bash", args, cwd: root, preview: true },
          };
        }

        onUpdate?.({
          content: [{ type: "text", text: `Running ${commandText}` }],
          details: { phase: "execute", command: "bash", args },
        });

        const result = await pi.exec("bash", args, { cwd: root, signal });
        if (result.killed) {
          throw new Error("Packaging command was cancelled");
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

        return {
          content: [{ type: "text", text: output }],
          details: { command: "bash", args, cwd: root, code: result.code },
        };
      },
    },
  ];
}

module.exports = factory;
module.exports.default = factory;
