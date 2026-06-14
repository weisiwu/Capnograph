<!-- context-seed:start -->
# AppTextsEnglish

## 定位

- ID: `EN-ATE`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:133`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AppTextsEnglish` 定义了 iOS 端所有英文界面文本的键值对枚举。
- 包含约 100 个 case，与 `AppTextsChinese` 的 key 一一对应。
- 每个 case 的 rawValue 为对应的英文字符串。
- 通过 `getTextByKey()` 根据当前语言选择中文或英文版本。
- 与 Android 端 `strings.xml` 资源文件对应。
<!-- context-seed:end -->
