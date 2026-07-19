---
title: CapnoEasy BLE 与运行时链路
category: documentation
tags:
  - area/architecture
  - area/bluetooth
  - area/runtime
summary: 解释扫描连接、初始化任务队列、协议解析、实时状态扇出和生命周期边界。
created: 2026-07-19
updated: 2026-07-19
owner: Android 与 iOS 负责人
review_cycle: 每次 BLE、协议、状态或报警改动
source_commit: edfd024010878ede15ae0d16c58308adc8eebef7
metadata_status: curated
search:
  boost: 1.2
---

# CapnoEasy BLE 与运行时链路

<div class="doc-status" markdown>
<span>连接</span><span>协议</span><span>实时状态</span>
</div>

## 连接与初始化时序

`BlueToothKit`/`BluetoothManager` 负责扫描、连接、服务发现、通知订阅、写特征值、断开和重连。Android 的 `BlueToothTaskQueueKit` 串行执行命令，避免配置命令互相覆盖。

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
sequenceDiagram
    accTitle: 设备连接与初始化时序
    accDescr: 用户或应用触发扫描，移动端连接设备、发现服务并订阅通知。任务队列停止连续数据，串行读取或设置关键参数，最后恢复连续数据。

    participant U as 操作者 / 自动连接
    participant A as Android / iOS 应用
    participant B as BLE 管理器
    participant Q as 命令任务队列
    participant D as CO₂ 监测设备

    U->>A: 选择设备或恢复已配对设备
    A->>B: 扫描并发起连接
    B->>D: 建立连接、发现服务与特征值
    B->>D: 订阅接收通知
    B->>Q: 加入初始化任务
    Q->>D: 停止连续数据
    loop 串行初始化
        Q->>D: 读取或设置参数
        D-->>Q: 写入结果 / 回读帧
    end
    Q->>D: 恢复连续数据
    D-->>B: 波形与状态通知
    B-->>A: 连接成功、实时状态可用
```

<figcaption><strong>文字摘要：</strong>连接成功还不等于可监测；只有服务发现、通知订阅和串行参数初始化完成后，实时状态才可用。</figcaption>
</figure>

传输层只应保证“字节可靠到达和连接状态可解释”。UI 导航、患者持久化和报告逻辑不应继续堆入蓝牙管理器。

## 协议解析职责

原始通知经过帧长度与校验，再按 `SensorCommand` 和 DPI/状态位分发。解析结果包括连续 CO₂、EtCO₂、FiCO₂、RR、呼吸/窒息、电量、校零、适配器状态，以及单位、量程、报警范围、补偿和设备版本。

必须用协议样例锁定字节序、缩放因子、符号位、状态位和无效值；UI 不应二次猜测原始字节。

## 同一帧如何扇出

<figure class="wiki-diagram wiki-diagram--wide" markdown>

```mermaid
flowchart LR
    accTitle: 实时状态的一次扇出
    accDescr: 同一设备帧解析后同时影响实时波形、生命体征、报警判断和记录缓存；记录随后进入本地存储和报告。

    Frame["一帧设备数据"] --> Parsed["协议解析结果"]
    Parsed --> Wave["实时波形"]
    Parsed --> Vitals["EtCO₂ · FiCO₂ · RR"]
    Parsed --> Alarm["报警判断"]
    Parsed --> Buffer["记录缓存"]
    Buffer --> Store["分块持久化"]
    Store --> Report["历史 · PDF · 打印"]
```

<figcaption><strong>文字摘要：</strong>一个字段变化至少影响波形、数值、报警和记录四类消费者，评审范围必须覆盖下游报告。</figcaption>
</figure>

## 状态、并发与生命周期

- Android `BlueToothKit` 发布实时 Flow/State，`AppStateModel` 管理导航、设置、记录、患者和报告状态；
- iOS 以 `ObservableObject` / `@Published` 推送状态；
- Hilt 注入与全局单例并存，数据库恢复、Activity 重建和测试替身要确认使用同一运行时实例；
- UI collector 应绑定 Activity/Composable 生命周期，避免重复订阅；
- BLE 回调、Room I/O、PDF 位图和打印应在合适线程执行，主线程只更新 UI；
- 任务队列的取消、超时、断连和重试必须有明确终态；
- 相邻路径仍见 `GlobalScope`、`AsyncTask`、`runBlocking` 等历史模式，改动时应评估收敛。

## 连接改动的最低验证

1. 权限未决定、拒绝、系统设置返回；
2. 扫描无结果、连接超时、服务/特征缺失；
3. 初始化中断、设备拒绝命令、任务超时；
4. 监测中断连、有限重连、重新初始化和状态清理；
5. Android/iOS 使用同一回放帧得到一致关键字段。

完整异常状态图见[故障路径与恢复](../review/failure-paths.md)。
