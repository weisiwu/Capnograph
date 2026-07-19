---
title: "apps"
category: generated
tags:
  - area/code-intelligence
  - source/gitnexus
summary: "由 GitNexus 代码知识图谱生成的模块文档。"
source_commit: "edfd024010878ede15ae0d16c58308adc8eebef7"
generated_at: "2026-07-18T16:08:03.557Z"
metadata_status: generated
search:
  boost: 0.5
---

!!! info "GitNexus 自动生成"
    来源提交：`edfd024010878ede15ae0d16c58308adc8eebef7`；生成时间：`2026-07-18T16:08:03.557Z`。
    本页允许同步脚本覆盖；涉及行为判断时请回到当前源码、配置和测试核验。
# apps 模块

## 概述

`apps` 模块是 CapnoEasy 项目的移动端应用集合，包含两个平台的原生应用：Android 和 iOS。这两个应用共享相同的业务目标——为二氧化碳监测设备提供配套的移动端控制与数据管理功能，但各自基于平台特性采用不同的技术栈和架构实现。

## 子模块

- [Android 应用](apps-android.md)：基于 Jetpack Compose + Material3 构建，使用 Hilt 依赖注入、Room 本地存储、MPAndroidChart 图表库
- [iOS 应用](apps-ios.md)：基于 SwiftUI 构建，遵循 MVVM 架构模式

## 模块关系

两个子模块在功能上互为镜像，均实现以下核心能力：

- **蓝牙设备管理**：设备发现、连接、数据收发
- **实时数据展示**：CO₂ 波形图、参数仪表盘
- **配置管理**：报警阈值、显示参数、模块参数、系统设置
- **数据持久化**：历史记录存储与查询
- **报告生成**：PDF 报告导出（Android 使用 iTextPDF）
- **热敏打印**：Android 特有功能

## 跨模块工作流

### 蓝牙连接与数据初始化

```
Android: BlueToothKit.initCapnoEasyConection()
         → 发送停止连续指令 → 发送已保存数据 → 写入设备
iOS:     BluetoothManage.connectDevice()
         → 同步蓝牙管理器数据 → 开始数据接收
```

该流程确保设备连接后，应用与设备之间的数据状态保持一致，避免数据冲突或丢失。

### 实时波形数据处理

```
Android: BlueToothKit.handleCO2Waveform()
         → 更新 AppStateModel 中的波形数据
         → 触发 UI 重绘
iOS:     BluetoothManage 接收数据
         → 更新 ResultView 中的波形显示
```

两个平台均采用响应式数据流，蓝牙数据到达后立即更新 UI 状态，实现低延迟的实时波形显示。

### 报警配置同步

```
Android: AlertSettingActivity → RangeSelector → 更新报警参数
         → 通过蓝牙写入设备
iOS:     AlertConfigView → RangeSlider → 更新报警参数
         → 通过 BluetoothManage 写入设备
```

报警配置在应用端和硬件设备端保持双向同步，确保报警阈值的一致性。

### PDF 报告生成（Android 特有）

```
PDFKit.SaveChartToPdfTask()
→ 配置报告模板 → 添加波形图表 → 添加页脚水印 → 保存 PDF
```

Android 应用通过异步任务生成包含 CO₂ 波形图、统计数据和水印的专业 PDF 报告。

## 架构对比

| 维度 | Android | iOS |
|------|---------|-----|
| UI 框架 | Jetpack Compose | SwiftUI |
| 状态管理 | AppStateModel (ViewModel) | ObservableObject |
| 依赖注入 | Hilt | 手动注入 |
| 本地存储 | Room + SQLite | Core Data / UserDefaults |
| 蓝牙通信 | BlueToothKit | BluetoothManage |
| 图表库 | MPAndroidChart | 原生 SwiftUI 图表 |

## 关键依赖关系

- 两个应用均依赖 `capnoeasy/components` 中的 UI 组件（如 `RangeSelector`、`SettingList`）
- 共享 `capnoeasy/kits` 中的工具类（如 `BlueToothKit`、`PDFKit`、`ErrorReporter`）
- 共享 `capnoeasy/models` 中的数据模型（如 `AppStateModel`）

## 使用场景

1. **临床监测**：医护人员通过应用实时查看患者 CO₂ 波形和参数
2. **数据管理**：查看历史记录、生成 PDF 报告用于病历存档
3. **设备配置**：设置报警阈值、调整显示参数、校准设备
4. **故障排查**：通过蓝牙诊断工具和错误报告系统定位问题
