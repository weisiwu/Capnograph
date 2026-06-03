# HotmeltPinter.loadScaledBitmap

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L361）。
领域：打印函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`HotmeltPinter.loadScaledBitmap`
- ID / 别名：load scaled bitmap, 打印图片缩放
- 源文件：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：按最大宽高加载并缩放用户图片 Bitmap

## 补充职责

按最大宽高读取缩放用户图片。

## 关键 ID / 别名

- 定位别名：load scaled bitmap, 打印图片缩放
- 关键字段 / 方法：`getBitmapDimensions`、`calculateScaleFactor`、`Bitmap.Config.RGB_565`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`getBitmapDimensions`、`calculateScaleFactor`、`Bitmap.Config.RGB_565`。
- 直接源码入口：`hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt`

## 主要调用点

当前打印路径未直接调用。

## 注意事项

目标尺寸由调用方传入。

## 最小验证方式

`rg "loadScaledBitmap"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
