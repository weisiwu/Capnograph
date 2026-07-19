---
title: CapnoEasy 五分钟图解导览
category: documentation
tags:
  - area/medical-device
  - area/architecture
  - area/onboarding
summary: 用业务流程、系统边界、实时数据时序和记录时序快速理解 CapnoEasy。
created: 2026-07-19
updated: 2026-07-19
owner: 产品、研发与质量负责人
review_cycle: 每个发布候选版本
source_commit: edfd024010878ede15ae0d16c58308adc8eebef7
metadata_status: curated
search:
  boost: 8
hide:
  - navigation
---

# CapnoEasy 五分钟图解导览

<div class="doc-status" markdown>
<span>阅读时间：约 5 分钟</span><span>适合：新成员、评审人、交付人员</span><span>证据：当前分支源码</span>
</div>

!!! abstract "先建立共同认识"
    CapnoEasy 是连接二氧化碳监测设备与移动端工作流的双平台应用。它接收设备数据、呈现实时波形和指标、执行报警提示、保存监测记录，并将同一份记录用于历史、报告和输出。

## 一张图看懂产品边界

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: CapnoEasy 产品边界
    accDescr: 操作者通过 Android 或 iOS 应用连接监测设备，应用处理实时指标、报警和记录，并输出历史、PDF 与打印结果。

    Operator["设备操作者"] --> App["CapnoEasy<br/>Android / iOS"]
    Device["CO₂ 监测设备"] <--> App
    App --> Monitor["实时监测<br/>波形 · EtCO₂ · FiCO₂ · RR"]
    App --> Record["监测记录<br/>患者 · 时间 · 波形"]
    Monitor --> Alert["报警提示"]
    Record --> History["历史与详情"]
    History --> PDF["PDF 报告"]
    History --> Printer["Android 热敏打印"]
    History --> Backup["本地备份 / 恢复"]
```

<figcaption><strong>文字摘要：</strong>操作者通过应用连接设备，实时状态进入报警和记录，记录再进入历史、PDF、打印和备份；这不证明临床适应证或法规状态。</figcaption>
</figure>

## 一次完整业务旅程

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: 一次完整监测业务旅程
    accDescr: 操作者完成授权、连接、确认状态、录入患者信息、开始监测记录、停止记录，最后查看、导出或打印结果。

    A["准备与连接<br/>1 授权准备<br/>2 搜索连接<br/>3 初始化参数"]
    B["实时监测<br/>4 观察波形与指标<br/>5 录入患者信息"]
    C["记录保存<br/>6 开始记录<br/>7 分块保存<br/>8 停止并补写余量"]
    D["结果输出<br/>9 历史与详情<br/>10 PDF / 打印 / 备份"]
    A --> B --> C --> D
```

<figcaption><strong>文字摘要：</strong>一次旅程依次经过准备连接、实时监测、记录保存和结果输出；异常路径在审核手册展开。</figcaption>
</figure>

## 实时数据如何到达屏幕

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
sequenceDiagram
    accTitle: 实时数据从监测设备到移动端屏幕的时序
    accDescr: 应用连接设备并订阅通知，设备持续发送帧，协议层解析和更新状态，界面刷新波形与数值，报警层根据状态决定声音提示。

    participant O as 操作者
    participant A as Android / iOS 应用
    participant B as BLE 管理与任务队列
    participant D as CO₂ 监测设备
    participant P as 协议解析与状态
    participant U as 波形 / 数值 / 报警 UI

    O->>A: 选择或自动连接设备
    A->>B: 扫描、连接、发现服务
    B->>D: 订阅通知并串行初始化参数
    D-->>B: 连续发送 CO₂ 与状态帧
    B->>P: 校验、拆帧、按命令与状态位分发
    P->>P: 更新 CO₂、EtCO₂、FiCO₂、RR 与设备状态
    P-->>U: 发布实时状态
    U-->>O: 刷新波形、指标和连接反馈
    P-->>U: 根据报警责任链更新声音状态
```

<figcaption><strong>文字摘要：</strong>设备通知经 BLE、协议和状态层进入 UI 与报警；Android/iOS 都应保持相同字段语义。</figcaption>
</figure>

## 一次记录如何落到本地

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
sequenceDiagram
    accTitle: Android 监测记录持久化时序
    accDescr: 操作者开始记录后应用校验设备与患者信息，创建患者和记录。实时波形达到分块阈值时压缩写入 Room，停止时补写剩余数据并结束当前记录上下文。

    participant O as 操作者
    participant M as MainActivity
    participant S as LocalStorageKit
    participant V as AppStateModel
    participant C as EtCo2LineChart
    participant R as Room

    O->>M: 点击开始记录
    M->>M: 校验设备连接与患者字段
    M->>S: savePatient + saveRecord
    S->>R: 写入 Patient 与 Record
    S-->>C: currentRecordId 可用

    loop 实时监测期间
        V-->>C: totalCO2WavedDataFlow 更新
        alt 达到 chunk 阈值
            C->>C: 取头部数据并 GZIP 压缩
            C->>R: 写入 CO2Data(recordId, chunkIndex)
            R-->>C: 返回写入结果
            C->>V: 成功后移除已保存数据
        end
    end

    O->>M: 点击停止记录
    M->>S: stopRecord(剩余波形)
    S->>R: 补写不足一个 chunk 的数据
    S->>S: 清空 currentRecordId
```

<figcaption><strong>文字摘要：</strong>Android 创建记录后按阈值写入 chunk，停止时补写余量；`Record.endTime` 的停止更新仍是 P0 待确认项。</figcaption>
</figure>

## 接下来读什么

<div class="reading-paths" markdown>
<div markdown>
<span>产品、临床业务、交付</span>

从[业务领域与端到端流程](../business/domain-and-workflows.md)继续，理解术语、参与者、报警责任链和数据不变量。
</div>
<div markdown>
<span>Android、iOS、架构</span>

从[架构总览与数据契约](../architecture/technical-architecture.md)继续，再按需进入 BLE 或持久化专题。
</div>
<div markdown>
<span>评审、测试、质量、发布</span>

从[审核总览与发布门禁](../review/review-guide.md)继续，再进入清单、故障、患者数据或发布证据专题。
</div>
</div>
