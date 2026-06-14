<!-- context-seed:start -->
# HotmeltPinter

## 定位

- ID: `AND-HP`
- 类型: `class`
- 领域: apps
- 来源: `apps/android/hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt:152`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `HotmeltPinter` 是 Android 端的热敏打印核心类。
- 使用 USB/串口协议与热敏打印机通信。
- **核心功能**：
  - `printPDF()`: 打印 PDF 报告。
  - `printTicket()`: 打印小票。
  - `checkStatus()`: 检查打印机状态。
- 打印机状态通过 `Printer` 单例管理。
- 初始化由 `PrintProtocalKitManager` 管理。
<!-- context-seed:end -->
