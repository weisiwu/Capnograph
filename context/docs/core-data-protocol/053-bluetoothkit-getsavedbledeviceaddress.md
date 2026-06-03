# BlueToothKit.getSavedBLEDeviceAddress

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L307）。
领域：蓝牙函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`BlueToothKit.getSavedBLEDeviceAddress`
- ID / 别名：load paired device, 读取配对地址
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：从 SharedPreferences 读取历史 CapnoEasy 地址

## 补充职责

读取历史 CapnoEasy 地址。

## 关键 ID / 别名

- 定位别名：load paired device, 读取配对地址
- 关键字段 / 方法：`paired_device_address`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`paired_device_address`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt`, `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt`

## 主要调用点

搜索结束自动连接、主页启动自动连接。

## 注意事项

返回 nullable String。

## 最小验证方式

`rg "getSavedBLEDeviceAddress"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
