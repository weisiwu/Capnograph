<!-- context-seed:start -->
# PageScene

## 定位

- ID: `AND-PS`
- 类型: `enum class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:73`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `PageScene` 定义了 Android 端所有页面的枚举，每个枚举值携带一个 lambda 用于生成页面标题。
- **HOME_PAGE**: 主页 "CapnoGraph"。
- **SETTING_PAGE**: 设置页。
- **DEVICES_LIST_PAGE**: 设备列表页。
- **SYSTEM_CONFIG_PAGE**: 系统设置页。
- **ALERT_CONFIG_PAGE**: 报警参数设置页。
- **DISPLAY_CONFIG_PAGE**: 显示参数设置页。
- **MODULE_CONFIG_PAGE**: 模块参数设置页。
- **PRINT_CONFIG_PAGE**: 打印设置页。
- **HISTORY_LIST_PAGE**: 历史记录列表页。
- **HISTORY_DETAIL_PAGE**: 历史记录详情页。

## 调用链

- `NavBarComponentState.currentPage` 的类型为此枚举。
- `BaseActivity.pageScene` 和 `viewModel.updateCurrentPage()` 使用此枚举管理页面导航。
- 页面标题通过 `title(Context) -> String` lambda 实现多语言支持。
<!-- context-seed:end -->
