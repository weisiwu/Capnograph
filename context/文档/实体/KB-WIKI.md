---
entity_id: KB-WIKI
entity_name: CapnoEasyWiki
entity_type: knowledge-site
source:
  - mkdocs.yml
  - docs/
updated: 2026-07-19
---

# CapnoEasyWiki

## 定位

`KB-WIKI` 是受 Git 管理的 CapnoEasy 正式知识站。它用 Material for MkDocs 汇总行业背景、应用业务、技术架构、审核知识与代码模块参考；当前源码、配置、迁移和测试是应用行为的第一证据源，行业知识另以权威指南、标准和监管资料核验。

## 结构

- `docs/guide/`：五分钟图解导览；
- `docs/business/industry-background.md`：二氧化碳描记行业背景、核心参数、典型场景、主流/旁流技术和标准监管边界；
- `docs/business/domain-and-workflows.md`、`data-and-risks.md`：围绕 CapnoEasy 应用的参与者、端到端流程、数据对象与风险；
- `docs/architecture/`：架构总览、BLE 运行时、持久化与输出；
- `docs/review/`：发布门禁、领域清单、故障路径、患者数据生命周期和发布证据；
- `docs/generated/gitnexus/`：导航分类名为“代码模块参考”，11 个 GitNexus 自动页面按“仓库全景 → 移动应用 → 构建与交付 → 项目知识与 AI”组织，同级且至少有两个子项的分组通过 `nav-accordion.js` 互斥展开；单子项分组始终显示、不启用手风琴。页面由 `scripts/sync_gitnexus_wiki.py` 覆盖同步。

## 维护门禁

```bash
python3 scripts/sync_gitnexus_wiki.py --check
python3 scripts/check_wiki.py
.venv-docs/bin/mkdocs build --strict
```

中文搜索依赖 `jieba==0.42.1` 和 `docs_search_terms.txt`。图表查看工具位于 `docs/assets/javascripts/diagram-tools.js`；样式位于 `docs/assets/stylesheets/extra.css`。

## 审阅证据

FigJam 审阅板：<https://www.figma.com/board/C70CG1Stzl2D7lHLuOuSyA>。板内包含 7 张本轮浏览器截图、页面指标与逐项修复说明。
