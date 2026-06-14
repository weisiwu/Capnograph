<!-- context-seed:start -->
# getTextByKey

## 定位

- ID: `FN-GTBK`
- 类型: `function`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:271`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `getTextByKey(key: String) -> String` 是 iOS 端的多语言文本查询函数。
- 根据 `AppConfigManage.language` 的当前值，从 `AppTextsChinese` 或 `AppTextsEnglish` 中查找与 key 匹配的 case。
- 使用 Swift 反射（`Mirror`）将传入的 key 字符串与枚举 case 名称匹配。
- 如果匹配失败，返回 key 本身作为 fallback。

## 调用链

- 所有 View 中需显示文本的地方通过 `appConfigManage.getTextByKey(key: "xxx")` 调用。
- 与 Android 端的 `getString(resId, context)` 函数对应。
<!-- context-seed:end -->
