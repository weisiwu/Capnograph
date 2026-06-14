<!-- context-seed:start -->
# PrintSettingActivity

## 定位

- ID: `AND-PSA`
- 类型: `class` (BaseActivity)
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/pages/PrintSettingActivity.kt:45`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `PrintSettingActivity` 是 Android 端的打印参数设置页面（`PageScene.PRINT_CONFIG_PAGE`）。
- **配置项**：
  - 患者信息：姓名、性别、年龄、ID、科室、病床号。
  - 医院名称和报告名称。
  - PDF 输出格式开关（`isPDF`）。
  - PDF 模板模式（正式/简易）。
  - 水印文字、透明度、异常片段上下文秒数。
- 包含 `OutputType` 数据类（定义输出类型）。
- 更新后保存到 `LocalStorageKit` 和 `PrintSetting`。

## 调用链

- 从 `SettingActivity` 菜单点击进入。
- 与 iOS 端的打印设置对应。
<!-- context-seed:end -->
