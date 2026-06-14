<!-- context-seed:start -->
# LanguageTypes

## 定位

- ID: `AND-LT`
- 类型: `enum class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:99`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `LanguageTypes` 定义了 Android 端支持的语言。
- **CHINESE("中文")**: 简体中文。
- **ENGLISH("English")**: 英文。
- `cname` 属性存储语言的本地化显示名称。

## 调用链

- `AppState.language` 使用此枚举存储当前语言设置。
- `SystemSettingActivity.updateLanguage()` 根据此枚举切换应用语言。
- 与 iOS 端的 `Languages` 枚举对应。
<!-- context-seed:end -->
