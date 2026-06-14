<!-- context-seed:start -->
# AI Agent 使用说明

本项目启用了 `context-seed` 上下文工作流。AI Agent 接手本项目或处理下载后的 ZIP 包时，应主动使用下面流程，不需要用户额外说明。

## 首选流程

1. 先生成计划，不直接写入目标项目:

```bash
python -m context_seed prepare . --operation init --json > context-seed-plan.json
```

2. 读取 `context-seed-plan.json`，按 `ai_tasks` 顺序逐个处理列出的实体，先补充对应 `context/文档/实体/<id>.md`，再做总览验收。
   只修改 `planned_files[].content`，不要改 `path`、`mode` 或托管标记。
3. 确认计划后应用:

```bash
python -m context_seed apply . --plan-file context-seed-plan.json
```

## 提交后收尾

用户或 Agent 完成 `git commit` 后，应主动分析最近提交并同步上下文:

```bash
python -m context_seed prepare . --operation finalize --commit HEAD --json > context-seed-plan.json
python -m context_seed apply . --plan-file context-seed-plan.json
```

人工快速路径可用 `python -m context_seed finalize .`，但 Agent 自动化场景优先使用 `prepare` + `apply`。

## 处理项目 ZIP 包

如果用户提供的是项目 ZIP 包，先解压到工作目录，再从 `context-seed` 工具所在目录运行:

```bash
python -m context_seed prepare /path/to/unzipped-project --operation init --json > context-seed-plan.json
python -m context_seed apply /path/to/unzipped-project --plan-file context-seed-plan.json
```

如果 ZIP 解压后的项目没有 `.git`，但需要提交后收尾，应先在目标目录初始化 git 并创建初始提交。

## 约束

- `apply` 只允许写入目标项目内相对路径，拒绝绝对路径和包含 `..` 的路径。
- 处理需求时先从 `context/实体标识映射.md` 解析唯一 ID，再加载该 ID 对应的 `context/文档/实体/<id>.md`。
- 先验证源码和上下文，再修改文档；不要只凭生成内容推断行为。
- 修改后同步 `context/实体标识映射.md`、相关实体上下文、`context/项目上下文.md` 和 `context/项目记忆.md`。
- 若安装了 Graphify 或 GitNexus，可在工作结束时按需刷新索引。
<!-- context-seed:end -->
