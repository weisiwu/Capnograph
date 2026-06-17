This repository is the CapnoGraph monorepo. Treat the repository root as the working directory, with Android under `apps/android` and iOS under `apps/ios`.

Before changing project behavior, first resolve names through `context/entity-id-mapping.md` or `context/实体标识映射.md`, then load the referenced supplemental context file. Older Android context rows may use paths relative to the former Android root; normalize those paths under `apps/android/`.

Prefer the project skills `capno-context`, `capno-android`, `capno-ios-parity`, `capno-packaging`, and `capno-packflow` when the request matches their descriptions. Use the custom tool `capno_context_lookup` for entity/context lookup and `capno_package` for legacy packaging previews/execution.
For Android 打包 + 产物发送场景, use `capno_packflow` first.
