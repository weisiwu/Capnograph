<!-- context-seed:start -->
# GENDER

## 定位

- ID: `AND-G`
- 类型: `enum class`
- 领域: apps
- 来源: `apps/android/app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt:108`
- 实体映射: `context/实体标识映射.md`

## 上下文

- `GENDER` 定义了患者性别枚举。
- **MALE("男")**: 男性。
- **FORMALE("女")**: 女性（注意拼写为 FORMALE 而非 FEMALE）。
- 用于 PDF 报告的患者信息字段。

## 调用链

- `AppState.patientGender` 的类型为此枚举。
- `MainActivity` 在加载打印设置时从 `PrintSetting` 转换性别值。
- `PrintSettingActivity` 中的表单使用此枚举供用户选择。
<!-- context-seed:end -->
