# ImageSelectorKit

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L232）。
领域：媒体。
聚合章节：Kit 与服务。

## 实体定位

- 实体：ImageSelectorKit
- ID / 别名：image picker, 图片选择
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：图片选择辅助

## 补充职责

图片私有存取工具。

## 关键 ID / 别名

- 定位别名：image picker, 图片选择
- 关键字段 / 方法：`saveImageToInternalStorage`、`loadImageFromInternalStorage`、`logoImgName`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`saveImageToInternalStorage`、`loadImageFromInternalStorage`、`logoImgName`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt`

## 主要调用点

打印设置/Logo 选择。

## 注意事项

保存目录是 `context.getDir("images", MODE_PRIVATE)`。

## 最小验证方式

检查 `ImageSelectorKit.kt`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
