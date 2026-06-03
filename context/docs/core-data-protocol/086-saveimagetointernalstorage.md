# saveImageToInternalStorage

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L340）。
领域：媒体函数。
聚合章节：核心函数与方法。

## 实体定位

- 实体：`saveImageToInternalStorage`
- ID / 别名：save print image, 保存内部图片
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：将用户选择图片压缩为 JPEG 存入 app 私有目录

## 补充职责

将 Uri 图片压缩为私有 JPEG。

## 关键 ID / 别名

- 定位别名：save print image, 保存内部图片
- 关键字段 / 方法：`context.getDir("images")`、`Bitmap.CompressFormat.JPEG`、quality 90。

## 关键字段 / 方法

- 主要字段、方法或协议值：`context.getDir("images")`、`Bitmap.CompressFormat.JPEG`、quality 90。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt`

## 主要调用点

打印 Logo 选择。

## 注意事项

未显式关闭 inputStream。

## 最小验证方式

检查函数

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
