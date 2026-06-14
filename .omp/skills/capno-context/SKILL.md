---
name: capno-context
description: CapnoGraph/CapnoEasy project context workflow. Use before answering, debugging, editing, or reviewing work that mentions project entities, IDs, Android/iOS files, BLE, CO2 waveform, PDF, printing, Gradle, Xcode, packaging, or context-seed docs.
globs:
  - "context/**"
  - "apps/android/**"
  - "apps/ios/**"
  - "AGENTS.md"
  - "README.md"
---

# CapnoGraph Context Workflow

Use this skill to ground work in the repository's existing context instead of guessing.

## Project Shape

- Repository root: CapnoGraph monorepo.
- Android project: `apps/android`.
- iOS project: `apps/ios`.
- Shared context: `context`.
- Root packaging entrypoint: `scripts/package.sh`.
- Project agent instructions: `AGENTS.md`.

Some Android context files were generated before the monorepo move and still show paths like `app/src/...`, `hotmeltprint/...`, `settings.gradle.kts`, or `gradle/libs.versions.toml`. In this repository, normalize those to `apps/android/app/src/...`, `apps/android/hotmeltprint/...`, `apps/android/settings.gradle.kts`, and `apps/android/gradle/libs.versions.toml`.

## Required Lookup Order

1. Search `context/entity-id-mapping.md` and `context/实体标识映射.md` for the user's entity name, class, enum, page, protocol value, resource, config file, or Chinese display name.
2. Record the entity, ID or alias, domain, source file, and supplemental context path.
3. Load only the relevant supplemental context under `context/docs/**` or `context/文档/实体/**`.
4. Verify current behavior in the source file before editing.
5. When behavior, fields, IDs, source paths, assets, or workflows change, update the relevant context docs and mapping rows.

Use `capno_context_lookup` when available. It searches the locator files and returns candidate source/context paths with Android monorepo path normalization.

## Context-Seed Rules

- Respect `AGENTS.md`.
- For broad initialization/finalization work, prefer `python -m context_seed prepare ... --json` followed by `python -m context_seed apply ...`.
- After a commit, run the finalize flow from `AGENTS.md` if asked to handle post-commit context sync.
- Do not rewrite unrelated context files. Keep manual notes outside `context-seed` managed blocks when possible.

## Common Domains

- App/UI workflow docs: `context/docs/app-ui-workflows.md` and `context/docs/app-ui-workflows/`.
- Core data, BLE protocol, PDF, storage, printing: `context/docs/core-data-protocol.md` and `context/docs/core-data-protocol/`.
- Build/platform/resources: `context/docs/build-platform.md`, `context/docs/platform-resources.md`, and `context/docs/platform-resources/`.
- Generated entity docs: `context/文档/实体/`.

## Answering Discipline

- State the entity/context files used when it helps.
- If source and context disagree, trust source and update context when the task changes durable behavior.
- For project changes, mention the validation run and whether context docs were updated.
