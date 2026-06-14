---
name: context-aware-dev
description: 面向代码仓库的上下文感知开发流程。用于回答、调试、实现或评审依赖项目实体、ID、配置、文档或历史上下文的问题，要求先从定位文件解析实体和 ID，再加载相关补充文档，完成任务后同步上下文文档。
---

# 上下文感知开发

使用本技能时，不要只凭记忆处理项目问题。先锚定仓库中的上下文定位文件，只解析当前请求需要的实体或 ID，加载该 ID 对应的最小上下文片段，再处理任务。

当项目安装了 GitNexus 或 Graphify 时，将它们作为辅助导航工具。它们可以帮助定位潜在修改点、相关执行流和影响范围，但不能替代对代码、配置和文档的直接验证。

接手项目时，先读取根目录 `AGENTS.md`。`context-seed` 会在初始化目标项目时生成或更新该文件，让 Agent 能自主知道应使用两阶段工具流。

如果当前仓库安装了 `context-seed`，优先使用两阶段 Agent 工具流:

1. 先运行 `python -m context_seed prepare . --operation init --json` 或 `python -m context_seed prepare . --operation finalize --commit HEAD --json`，获取机器可读计划。
2. 根据计划中的 `project_summary`、`baseline_contents`、`commit_analysis` 和 `ai_tasks` 补充 `planned_files[].content`；初始化计划必须按 `ai_tasks` 顺序逐个处理实体上下文，再做总览验收。
3. 确认计划后运行 `python -m context_seed apply . --plan-file context-seed-plan.json` 写入。

`apply` 只允许目标项目内相对路径，拒绝绝对路径和包含 `..` 的路径。

## 速度策略

- 识别用户显式指定的层级: `Fast`、`Standard`、`Deep`、`快速`、`标准` 或 `深度`。未指定时使用 `Fast`。
- 在 `Fast` 中，优先使用定向 `rg` 和章节读取；打开源码前最多执行一次图查询。
- 代码符号、调用方、执行流和影响面优先使用 GitNexus。概念查找、文档密集问题和关系/路径探索优先使用 Graphify。
- 如果所选层级对共享代码、数据模式、生成资产、用户流程或大范围重构过浅，在高风险编辑前建议升级到 `Standard` 或 `Deep`。
- 项目文件发生变化时，只在工作结束时刷新一次 GitNexus/Graphify。

## 工作流程

1. 使用定向 `rg`，从 `context/实体标识映射.md`、`context/*映射*.md`、`context/项目上下文.md`、`context/项目记忆.md` 和 `docs/` 中解析请求涉及的实体与 ID。
2. 记录一个小工作集: 实体、唯一 ID、领域、源码文件和补充上下文路径。
3. 优先只加载映射行中 `补充上下文` 指向的 `context/文档/实体/<id>.md`；需要仓库总览时再读 `context/文档/代码库上下文.md`。
4. 仅当直接定位文档有歧义或影响面重要时，使用 GitNexus 或 Graphify:
   - GitNexus: `gitnexus query`、`gitnexus impact`、`gitnexus detect-changes`，或 `npx -y gitnexus@latest ...`。
   - Graphify: `graphify query "<entity>" --budget 800`、`graphify explain "<symbol>"`，或 `graphify path "<A>" "<B>"`。
5. 编辑前用源码文件验证图工具输出。
6. 做最小安全修改，并运行最小有意义的验证。
7. 修改后同步受影响的上下文文档:
   - 当 ID、源码文件或补充文档变化时，更新 `context/实体标识映射.md`。
   - 当行为、字段、资产、流程或假设变化时，更新相关 `context/文档/实体/<id>.md` 文件。
   - 如果 `docs/` 中存在同一信息的镜像内容，也保持一致。
8. 如果项目文件发生变化，结束时刷新一次可选索引:
   - GitNexus: `gitnexus analyze --no-stats`
   - Graphify: `graphify update . --no-cluster`
9. 用户完成 `git commit` 后，运行提交后收尾:
   - Agent 推荐命令: `python -m context_seed prepare . --operation finalize --commit HEAD --json`
   - 人工快捷命令: `python -m context_seed finalize .`
   - 收尾动作: 分析最近提交，更新 `context/项目上下文.md`、`context/项目记忆.md`、`context/文档/代码库上下文.md` 和实体映射。

## 输出要求

- 在有助于理解时说明关键实体或 ID。
- 说明已运行的验证。
- 说明更新了哪些上下文文档，或明确无需同步上下文。
- 说明是否使用 GitNexus 和 Graphify，以及是否刷新了索引。
