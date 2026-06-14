<!-- context-seed:start -->
# LocalizedText

## 定位

- ID: `EN-LT`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:231`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LocalizedText` 是 iOS 端文本键名的枚举定义。
- 每个 case 对应一个文本 key（如 `case SearchConfirmYes = "SearchConfirmYes"`），
  rawValue 作为索引键用于在两个语言枚举中查找对应文本。
- `AppTextsChinese` 和 `AppTextsEnglish` 的每个 case 对应一个 `LocalizedText` key。
- `getTextByKey(key: String)` 内部使用此枚举进行键名匹配。

## 使用建议

- 添加新文本时，先在 AppTextsChinese/AppTextsEnglish 中添加对应 case，
  然后在 LocalizedText 中添加键名 case。
<!-- context-seed:end -->
