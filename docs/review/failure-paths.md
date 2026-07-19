---
title: CapnoEasy 故障路径与恢复
category: documentation
tags:
  - area/reliability
  - area/bluetooth
  - area/persistence
summary: 用三张故障流程图明确 BLE、记录写入和备份恢复的失败状态、用户反馈与关闭证据。
created: 2026-07-19
updated: 2026-07-19
owner: Android、iOS、测试与质量负责人
review_cycle: 每次连接、记录或存储改动
source_commit: edfd024010878ede15ae0d16c58308adc8eebef7
metadata_status: curated
search:
  boost: 1.3
---

# CapnoEasy 故障路径与恢复

<div class="doc-status" markdown>
<span>异常路径</span><span>故障注入</span><span>可恢复状态</span>
</div>

!!! warning "图的性质"
    本页给出审核目标状态，不把尚未由测试证明的恢复行为写成“当前已实现”。每条路径都应由真机、回放或故障注入证据关闭。

## BLE 权限、断连与重连

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
stateDiagram-v2
    accTitle: BLE 权限拒绝、断连和重连状态
    accDescr: 权限未决定时请求授权，拒绝后停止扫描并提供设置入口。连接后若断连，应用停止使用陈旧数据，进入有限次数重连；超限后回到可操作的未连接状态。

    [*] --> 权限检查
    权限检查 --> 请求授权: 未决定
    请求授权 --> 未连接: 允许
    请求授权 --> 权限受限: 拒绝
    权限受限 --> 权限检查: 用户从系统设置返回
    未连接 --> 扫描中: 主动扫描或自动连接
    扫描中 --> 已连接: 连接与初始化成功
    扫描中 --> 未连接: 超时或取消
    已连接 --> 重连中: 意外断连
    重连中 --> 已连接: 重连与重新初始化成功
    重连中 --> 未连接: 超过重试上限
```

<figcaption><strong>文字摘要：</strong>拒绝权限必须停止扫描并给出设置入口；断连后不能继续显示陈旧“已连接”状态，重连必须有限次且重新初始化。</figcaption>
</figure>

审核证据：权限拒绝录像、系统设置返回、扫描超时、监测中断电、重连后参数回读、重试上限与报警声音状态。

## chunk 写入失败

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: 波形 chunk 写入失败与恢复
    accDescr: 波形达到阈值后生成带记录标识和索引的快照。数据库提交成功后才能移除内存数据；失败时保留原数据、记录最小诊断信息并有限重试，最终失败时停止记录并提示用户。

    Buffer["内存波形达到阈值"] --> Snapshot["固定 recordId · chunkIndex · 数据快照"]
    Snapshot --> Write["压缩并写入 Room"]
    Write --> Result{"事务结果"}
    Result -->|成功| Commit["递增索引 · 移除已保存数据"]
    Result -->|失败| Keep["保留原缓冲 · 记录最小诊断字段"]
    Keep --> Retry{"仍可安全重试？"}
    Retry -->|是| Write
    Retry -->|否| Stop["停止记录 · 保留可恢复数据 · 明确提示"]
```

<figcaption><strong>文字摘要：</strong>数据库成功提交是移除内存波形的唯一前提；失败要保留原数据并避免重复 chunk。</figcaption>
</figure>

审核证据：磁盘不足、数据库异常、压缩异常、重复重试、应用后台与停止记录并发；验证 `recordId + chunkIndex` 唯一且无丢点。

## 备份、恢复与迁移失败

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: 备份恢复与数据库迁移失败路径
    accDescr: 恢复前先校验来源、版本和空间并冻结写入，再创建原库安全副本。替换后执行迁移和数据校验；任一步失败都回滚原库并重建数据库实例，成功后才解除冻结。

    Select["用户选择备份"] --> Preflight["校验来源 · 版本 · 空间 · 哈希"]
    Preflight -->|失败| Reject["拒绝恢复且不改原库"]
    Preflight -->|通过| Freeze["冻结新写入 · 关闭数据库"]
    Freeze --> Safety["保存原库安全副本"]
    Safety --> Replace["替换主库 / WAL / SHM"]
    Replace --> Migrate["执行 Migration"]
    Migrate --> Verify{"记录 · 患者 · chunk · 外键校验"}
    Verify -->|通过| Reopen["重建实例 · 解除冻结"]
    Verify -->|失败| Rollback["回滚原库 · 重建实例"]
```

<figcaption><strong>文字摘要：</strong>恢复必须先冻结写入并保留原库副本；替换、迁移或校验失败时都回滚，不能留下半迁移状态。</figcaption>
</figure>

审核证据：上一发布版本真实数据库、损坏备份、版本过新、空间不足、恢复中断、WAL/SHM 一致性、回滚后的记录数量与 chunk 连续性。

## 最小验收矩阵

| 路径 | 必须观察的状态 | 必须证明的数据性质 |
|---|---|---|
| 权限拒绝 | 无扫描、无假连接、设置入口清晰 | 不产生患者或设备残留数据 |
| 断连重连 | 状态及时、重试有限、参数重新初始化 | 不把断连期间陈旧值写入新记录 |
| chunk 写失败 | 用户可知、可重试或安全停止 | 不丢点、不重复、不跨记录 |
| 恢复失败 | 原库可回滚、实例可重新打开 | 患者、记录、chunk 和外键一致 |

下一步回到[领域审核清单](domain-checklists.md)，把本页场景关联到实际测试编号。
