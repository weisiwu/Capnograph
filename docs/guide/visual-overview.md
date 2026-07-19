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
---

# CapnoEasy 五分钟图解导览

<div class="doc-status" markdown>
<span>阅读时间：约 5 分钟</span><span>适合：新成员、评审人、交付人员</span><span>证据：当前分支源码</span>
</div>

!!! abstract "先建立共同认识"
    CapnoEasy 的 Android 和 iOS 都连接二氧化碳监测设备，但两端不共享一套应用架构。Android 当前使用 Room 持久化记录，并支持历史、PDF、热敏打印和数据库备份；iOS 当前以内存波形生成 PDF。共享的是设备协议和业务语义，不是实现结构。

## 一张图看懂产品边界

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: CapnoEasy 产品边界
    accDescr: 操作者可通过 Android 或 iOS 连接设备并查看实时指标。Android 将记录持久化到 Room，再进入历史、PDF、打印和备份；iOS 把当前波形保留在内存中并生成 PDF。

    Operator["设备操作者"] --> Android["Android 应用"]
    Operator --> iOS["iOS 应用"]
    Device["CO₂ 监测设备"] <--> Android
    Device <--> iOS
    Android --> AMonitor["实时监测与报警"]
    iOS --> IMonitor["实时监测与报警"]
    Android --> Room["Room 记录"]
    Room --> AOutput["历史 · PDF · 打印 · 备份"]
    iOS --> Memory["当前内存波形"]
    Memory --> IOutput["PDF 输出"]
```

<figcaption><strong>文字摘要：</strong>两端都能连接设备并呈现实时数据；Android 的记录与输出经过 Room，iOS 当前经过内存波形生成 PDF，两条路径不得合并描述。</figcaption>
</figure>

## 一次完整业务旅程

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: Android 与 iOS 的监测业务分支
    accDescr: 两端都经过授权、连接和实时监测。Android 进入患者记录、Room 分块持久化、历史和多种输出；iOS 将当前波形保留在内存中并导出 PDF。

    Common["共同起点<br/>授权 · 连接 · 实时监测"]
    Common --> AndroidRecord["Android<br/>患者与记录"]
    AndroidRecord --> RoomRecord["Room chunk 持久化"]
    RoomRecord --> AndroidOutput["历史 · PDF · 打印 · 备份"]
    Common --> IOSRecord["iOS<br/>当前波形内存累积"]
    IOSRecord --> IOSOutput["PDF 输出"]
```

<figcaption><strong>文字摘要：</strong>两端在实时监测后分叉：Android 进入持久化历史与多输出链路，iOS 当前进入内存波形与 PDF 链路。</figcaption>
</figure>

## 实时数据如何到达屏幕

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: Android 与 iOS 的实时数据路径
    accDescr: 同一设备协议在 Android 经 BlueToothKit 解析并更新 AppStateModel，再进入 Compose；在 iOS 经 BluetoothManager 的 CoreBluetooth delegate 解析并更新 Published 状态，再进入 SwiftUI。

    Device["CO₂ 监测设备"] --> AndroidBLE["Android<br/>BlueToothKit"]
    AndroidBLE --> Queue["BluetoothTaskQueue"]
    AndroidBLE --> AndroidState["AppStateModel / AppState"]
    AndroidState --> Compose["Compose 波形 · 指标 · 报警"]

    Device --> IOSBLE["iOS<br/>BluetoothManager + CoreBluetooth"]
    IOSBLE --> IOSState["@Published 运行时状态"]
    IOSState --> SwiftUI["SwiftUI 波形 · 指标 · 报警"]

    Contract["共享语义<br/>UUID · 命令 · 缩放 · 状态位"] -.-> AndroidBLE
    Contract -.-> IOSBLE
```

<figcaption><strong>文字摘要：</strong>Android 和 iOS 分别通过自己的 BLE、状态和 UI 链路到达屏幕；需要一致的是协议解析结果，不是类或分层形态。</figcaption>
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

先从[行业与业务背景](../business/industry-background.md)理解二氧化碳描记、典型场景和技术路线，再进入[应用业务与端到端流程](../business/domain-and-workflows.md)查看 CapnoEasy 的参与者、报警责任链和数据不变量。
</div>
<div markdown>
<span>Android 架构</span>

从 [Android 架构](../architecture/android-architecture.md) 阅读 Activity/Compose、全局状态、BLE Kit、Room 和输出链路。
</div>
<div markdown>
<span>iOS 架构</span>

从 [iOS 架构](../architecture/ios-architecture.md) 阅读 SwiftUI、EnvironmentObject、CoreBluetooth、内存历史和 PDF 链路。
</div>
</div>
