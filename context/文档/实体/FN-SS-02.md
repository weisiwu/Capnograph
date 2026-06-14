<!-- context-seed:start -->
# stopScanning

## 定位

- ID: `FN-SS-02`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1362`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `stopScanning()` 是停止蓝牙扫描的函数。
- 调用 `CBCentralManager.stopScan()` 停止 BLE 扫描。
- 与 `startScanning()` 成对使用。
<!-- context-seed:end -->
