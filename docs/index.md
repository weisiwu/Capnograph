---
title: CapnoEasy Wiki 总览
category: documentation
tags:
  - area/mobile
  - area/medical-device
  - area/knowledge-management
summary: CapnoEasy 行业背景、应用业务、技术架构、审核和代码模块参考的统一入口。
created: 2026-07-19
updated: 2026-07-19
owner: 项目知识库维护者
review_cycle: 每个发布候选版本
source_commit: edfd024010878ede15ae0d16c58308adc8eebef7
metadata_status: curated
search:
  boost: 1.8
hide:
  - navigation
---

<div class="wiki-hero" markdown>
<span class="wiki-kicker">CAPNOEASY KNOWLEDGE BASE</span>

# 把设备、数据和交付讲清楚

CapnoEasy 项目知识库把业务语义、双平台架构、审核门禁和代码图谱连成一条可追溯的知识链。第一次进入建议先看图解导览；准备改代码或发版本时，直接进入对应手册。

[五分钟图解导览](guide/visual-overview.md){ .md-button .md-button--primary }
[查看发布门禁](review/review-guide.md#release-gate-flow){ .md-button }
</div>

<div class="wiki-status" markdown>
<div>
<span>当前证据基线</span>
<strong>当前分支源码</strong>
</div>
<div>
<span>已审核知识域</span>
<strong>业务 · 架构 · 审核</strong>
</div>
<div>
<span>本轮页面复核</span>
<strong>2026-07-19</strong>
</div>
</div>

!!! warning "证据优先级"
    当前源码、配置、数据库迁移和测试是应用行为的第一证据源。行业知识使用权威指南、标准和监管资料，并与产品预期用途分开；`代码模块参考`用于导航和理解代码，但不能替代源码核验。

## 按任务选择入口

<div class="wiki-card-grid" markdown>
<div class="wiki-card wiki-card--featured" markdown>
<span class="wiki-card__eyebrow">第一次来 · 约 5 分钟</span>

**[先看图解导览](guide/visual-overview.md)**

用产品边界图、业务流程图、实时数据时序和记录持久化时序，快速建立对 CapnoEasy 的整体认识。
</div>
<div class="wiki-card" markdown>
<span class="wiki-card__eyebrow">行业 · 临床背景 · 产品边界</span>

**[行业与业务背景](business/industry-background.md)**

二氧化碳描记、核心参数、典型场景、主流/旁流技术和标准监管视角。
</div>
<div class="wiki-card" markdown>
<span class="wiki-card__eyebrow">CapnoEasy · 用户 · 应用流程</span>

**[应用业务与端到端流程](business/domain-and-workflows.md)**

围绕当前应用介绍参与者、连接、监测、报警、记录、报告和数据边界。
</div>
<div class="wiki-card" markdown>
<span class="wiki-card__eyebrow">Android · iOS · 架构</span>

**[架构总览与数据契约](architecture/technical-architecture.md)**

双平台框架与跨平台契约；BLE、持久化和输出分别进入专题页。
</div>
<div class="wiki-card" markdown>
<span class="wiki-card__eyebrow">评审 · 测试 · 质量 · 发布</span>

**[审核总览与发布门禁](review/review-guide.md)**

风险分级、当前阻断项，以及清单、故障、隐私和发布证据专题入口。
</div>
<div class="wiki-card" markdown>
<span class="wiki-card__eyebrow">文档 · 自动化</span>

**[文档生成与维护](contributing.md)**

MkDocs 构建、代码模块参考同步和内容所有权。
</div>
</div>

## 从业务动作到交付结果

<figure class="wiki-diagram" markdown>

```mermaid
flowchart LR
    accTitle: CapnoEasy 从设备到交付的主线
    accDescr: 监测设备数据进入移动应用，形成实时监测和报警状态。操作者开始记录后，数据被保存为历史记录并用于报告、打印与备份。

    Device["监测设备"] --> Live["实时波形与指标"]
    Live --> Alert["报警提示"]
    Live --> Session["监测记录"]
    Session --> History["历史与详情"]
    History --> Output["PDF · 打印 · 备份"]
```

<figcaption><strong>文字摘要：</strong>设备数据先形成实时状态和报警，记录后进入历史，并从同一记录快照生成 PDF、打印与备份。</figcaption>
</figure>

## 知识如何形成

<figure class="wiki-diagram" markdown>

```mermaid
flowchart TB
    accTitle: CapnoEasy 知识形成与发布路径
    accDescr: 权威指南、标准和监管资料进入行业背景，源码、配置、数据库迁移和测试进入应用手册和 GitNexus 代码图谱，最终由 MkDocs 汇总为可搜索的静态知识站。

    External["临床指南 / 标准 / 监管资料"] --> Industry["行业与业务背景"]
    Source["源码 / 配置 / 迁移 / 测试"] --> Curated["业务 / 架构 / 审核人工手册"]
    Source --> Graph["GitNexus 代码知识图谱"]
    Graph --> Generated["自动生成模块说明"]
    Industry --> Review["人工复核"]
    Curated --> Review["人工复核"]
    Generated --> Review
    Review --> Site["MkDocs 静态知识站"]
```

<figcaption><strong>文字摘要：</strong>行业背景来自外部权威资料，应用行为来自当前代码证据；人工手册和代码图谱经复核后由 MkDocs 汇总，两类证据不能互相替代。</figcaption>
</figure>

### 人工维护区

- `docs/business/`：领域术语、业务对象和端到端流程；
- `docs/architecture/`：技术框架、平台边界和数据契约；
- `docs/review/`：代码、测试、隐私和发布审核知识。

这些页面受 Git 管理，不允许自动生成脚本覆盖。

### 自动生成区

`docs/generated/gitnexus/` 来自本地 `.gitnexus/wiki/`，由 `scripts/sync_gitnexus_wiki.py` 同步。页面带有生成提交和时间元数据；重新生成后必须复核事实和链接。

## 当前重点风险

!!! danger "发布与合并前优先处理"
    当前基线包含 **2 项 P0 阻断**和 **5 项 P1 高风险**。详细证据、关闭条件和最低测试组合见[审核与评审指南](review/review-guide.md#baseline-findings)。

- Android EtCO₂/RR 报警区间判断需要业务规则和边界测试确认；
- 停止记录时的 `Record.endTime` 更新链路需要确认；
- 实时波形 chunk 当前使用带临时标记的 100 点参数；
- Room v2 schema、迁移和备份恢复测试仍需补齐；
- 患者数据、敏感权限和诊断上报需要持续做最小化审核。
