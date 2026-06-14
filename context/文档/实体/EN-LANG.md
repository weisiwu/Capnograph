<!-- context-seed:start -->
# Languages

## 定位

- ID: `EN-LANG`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:4`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `Languages` 是 iOS 端支持的语言枚举。
- **case Chinese**: 中文（rawValue 为"中文"）。
- **case English**: 英文（rawValue 为"English"）。
- 用于 `AppConfigManage.language` 属性，控制应用界面的中/英文切换。
- 与 Android 端的 `LanguageTypes` 枚举对应。

## 调用链

- `SystemConfigView` 中的 `RadioButtonGroup` + `RadioButton` 使用此枚举渲染语言选择 UI。
- 切换语言后，所有通过 `getTextByKey()` 获取的文本自动切换语言。
<!-- context-seed:end -->
