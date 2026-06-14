<!-- context-seed:start -->
# checkAlertRangeValid

## 定位

- ID: `FN-CARV`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/BluetoothManage.swift:770`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `checkAlertRangeValid(value: CGFloat, upper: CGFloat, lower: CGFloat) -> Bool` 是报警范围检查函数。
- 检查传入的值是否在指定的上下限之间。
- 在发送报警范围更新指令前进行有效性验证。
<!-- context-seed:end -->
