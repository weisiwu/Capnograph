<!-- context-seed:start -->
# AppTextsChinese

## 定位

- ID: `EN-ATC`
- 类型: `enum` (String)
- 领域: apps
- 来源: `apps/ios/CapnoGraph/AppConfigManage.swift:34`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `AppTextsChinese` 定义了 iOS 端所有中文界面文本的键值对枚举。
- 包含约 100 个 case，覆盖全部 UI 文本（标题、按钮、提示、标签等）。
- 每个 case 的 rawValue 为对应文本的中文字符串。
- 与 `AppTextsEnglish` 成对使用，通过 `getTextByKey()` 根据当前语言获取对应文本。
- 与 Android 端 `strings.xml` 资源文件对应。
<!-- context-seed:end -->
