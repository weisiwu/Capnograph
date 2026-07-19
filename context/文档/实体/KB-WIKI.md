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

`KB-WIKI` 是受 Git 管理的 CapnoEasy 正式知识站。它用 Material for MkDocs 汇总行业背景、应用业务、技术架构与代码模块参考；当前源码、配置、迁移和测试是应用行为的第一证据源，行业知识另以权威指南、标准和监管资料核验。

## 结构

- `docs/guide/`：五分钟图解导览；
- `docs/business/industry-background.md`：二氧化碳描记行业背景、核心参数、典型场景、主流/旁流技术和标准监管边界；
- `docs/business/domain-and-workflows.md`、`data-and-risks.md`：围绕 CapnoEasy 应用的参与者、端到端流程、数据对象与风险；
- 有实际子页面的业务、架构与代码参考栏目统一显示左侧整体结构，右侧显示当前页章节目录；三类栏目共用 `nav-accordion.js`，多子项大项互斥折叠，单子项大项常驻。首页和图解导览是单页入口，不生成空侧栏；
- `docs/architecture/android-architecture.md`：以 Android 源码为准记录 Activity/Compose、全局状态、BLE Kit、Room 和输出链路；
- `docs/architecture/ios-architecture.md`：以 iOS 源码为准记录 SwiftUI、EnvironmentObject、CoreBluetooth、内存历史和 PDF 链路；
- `docs/generated/gitnexus/`：导航分类名为“代码模块参考”，11 个 GitNexus 自动页面按“仓库全景 → 移动应用 → 构建与交付 → 项目知识与 AI”组织，页面由 `scripts/sync_gitnexus_wiki.py` 覆盖同步。

## 维护门禁

```bash
python3 scripts/sync_gitnexus_wiki.py --check
python3 scripts/check_wiki.py
.venv-docs/bin/mkdocs build --strict
```

中文搜索依赖 `jieba==0.42.1` 和 `docs_search_terms.txt`。图表查看工具位于 `docs/assets/javascripts/diagram-tools.js`；样式位于 `docs/assets/stylesheets/extra.css`。

## 审阅证据

FigJam 审阅板：<https://www.figma.com/board/C70CG1Stzl2D7lHLuOuSyA>。板内包含 7 张本轮浏览器截图、页面指标与逐项修复说明。
