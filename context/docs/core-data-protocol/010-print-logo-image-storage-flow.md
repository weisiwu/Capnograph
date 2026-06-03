# Print logo image storage flow

来源批次：蓝牙、协议、数据与业务服务实体补充。
定位入口：`context/entity-id-mapping.md`（L123）。
领域：媒体。
聚合章节：核心功能与业务流程。

## 实体定位

- 实体：Print logo image storage flow
- ID / 别名：print_logo.jpg, 图片选择, 内部图片
- 源文件：`app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt`
- 原始补充上下文：`.cursor/rules/project-memory.mdc`
- 备注：保存/读取打印设置使用的内部图片文件

## 补充职责

将打印设置使用的 Logo 图片保存到 app 私有 images 目录并读取。

## 关键 ID / 别名

- 定位别名：print_logo.jpg, 图片选择, 内部图片
- 关键字段 / 方法：`saveImageToInternalStorage`、`loadImageFromInternalStorage`、`logoImgName = "print_logo.jpg"`。

## 关键字段 / 方法

- 主要字段、方法或协议值：`saveImageToInternalStorage`、`loadImageFromInternalStorage`、`logoImgName = "print_logo.jpg"`。
- 直接源码入口：`app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt`

## 主要调用点

打印设置图片选择链路。

## 注意事项

保存格式固定 JPEG 质量 90；读取失败返回 null。

## 最小验证方式

`rg "print_logo.jpg|saveImageToInternalStorage"`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码与本文不一致，以当前源码为准，并修正文档。
