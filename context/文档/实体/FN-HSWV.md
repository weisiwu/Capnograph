<!-- context-seed:start -->
# handleSofrWareVersion

## 定位

- ID: `FN-HSWV`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:1073`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `handleSofrWareVersion(data: [UInt8])` 是软件版本信息处理函数。
- 解析设备返回的 0xE5 类型软件版本响应帧（注意函数名拼写为"SofrWare"而非"Software"）。
- 提取并更新 `appConfigManage.firmwareVersion`。
<!-- context-seed:end -->
