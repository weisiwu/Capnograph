<!-- context-seed:start -->
# ToastType

## 定位

- ID: `EN-TT`
- 类型: `enum`
- 领域: apps
- 来源: `apps/ios/CapnoGraph/ContentView.swift:3`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `ToastType` 定义了 Toast 消息的两种状态分类。
- **case SUCCESS**: 成功状态的 Toast，显示对勾图标 (`toast_success`)。
- **case FAIL**: 失败状态的 Toast，显示叉号图标 (`toast_fail`)。
- 由 `AppConfigManage.toastType` 持有，在 `ContentView.swift` 的 `BasePageView` 中渲染。
- 图标选择逻辑：`Image(type == ToastType.SUCCESS ? "toast_success" : "toast_fail")`。

## 调用链

- `AppConfigManage` 在设置 `toastMessage` 时同步设置 `toastType`。
- `BasePageView` 根据 `appConfigManage.toastMessage != ""` 展示 Toast，类型由此枚举控制。

## 使用建议

- 当请求命中本 ID、实体名、来源路径或领域时加载本文件。
- 如果添加新的 Toast 类型，同步增加 case 并更新图标资源。
<!-- context-seed:end -->
