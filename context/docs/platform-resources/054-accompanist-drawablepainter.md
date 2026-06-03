# Accompanist DrawablePainter

来源批次：平台、依赖、资源实体补充。
定位入口：`context/entity-id-mapping.md`（L459）。
领域：三方依赖。
实体级上下文：`context/docs/platform-resources/054-accompanist-drawablepainter.md`。

## 实体定位

- 实体：Accompanist DrawablePainter
- ID / 别名：`com.google.accompanist:accompanist-drawablepainter:0.37.0`
- 源文件：`app/build.gradle.kts`
- 原始补充上下文：`context/docs/build-platform.md`
- 关联总览文档：`context/docs/build-platform.md`
- 备注：app 模块声明的 drawable painter 辅助库

## 补充职责

Drawable 转 Compose Painter 的辅助依赖。

## 关键 ID / 别名

`com.google.accompanist:accompanist-drawablepainter:0.37.0`

## 关键字段 / 方法

`com.google.accompanist:accompanist-drawablepainter:0.37.0`。

## 主要调用点

app 声明依赖；源码未检出直接调用。

## 注意事项

移除前确认没有后续 drawable painter 需求。

## 最小验证方式

`./gradlew :app:assembleDebug`

## 同步要求

- 如果该实体的职责、ID / 别名、源文件、调用点或行为发生变化，同步更新本文档和 `context/entity-id-mapping.md`。
- 如果源码、构建配置或资源文件与本文不一致，以当前源码为准，并修正文档。
