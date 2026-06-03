# 核心数据、协议与业务服务实体索引

本文件是蓝牙、协议、数据与业务服务实体补充批次的索引。实体级补充上下文已拆分到 `context/docs/core-data-protocol/`，每个任务实体对应一个 Markdown 文档。

## 读取源文件

| 文件 | 覆盖内容 |
| --- | --- |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothKit.kt` | BLE/经典蓝牙扫描连接、GATT、命令发送、协议解析、报警状态、实时波形数据流。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/CapnoEasyProtocalKit.kt` | 传感器命令、BLE 服务/特征 UUID、80H/84H/F2H/CAH 字段枚举。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/BlueToothTaskQueueKit.kt` | BLE 串行任务队列。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/LocalStorageKit.kt` | Room 实体、DAO、数据库单例、波形压缩、记录和偏好设置存储。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/dbmigration/DatabaserMigration_FROM1_TO2.kt` | v1 到 v2 数据库迁移。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/DatabaseBackupHelperKit.kt` | 数据库 DB/WAL/SHM 备份与恢复。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/PDFKit.kt` | PDF 数据过滤、图表渲染和 iTextPDF 输出。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/AlertAudioKit.kt` | 低级/中级报警音播放。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/ImageSelectorKit.kt` | 打印 Logo 图片私有存储。 |
| `app/src/main/java/com/wldmedical/capnoeasy/kits/PrintProtocalKit.kt` | 热敏打印 SDK 单例初始化。 |
| `hotmeltprint/src/main/java/com/wldmedical/hotmeltprint/HotmeltPinter.kt` | GPrinter SDK 封装、小票打印和波形 Bitmap 生成。 |
| `app/src/main/java/com/wldmedical/capnoeasy/pages/SearchActivity.kt` | 搜索页扫描和连接调用点。 |
| `app/src/main/java/com/wldmedical/capnoeasy/pages/MainActivity.kt` | 记录开始/停止、打印偏好加载和数据库恢复触发。 |
| `app/src/main/java/com/wldmedical/capnoeasy/components/EtCo2LineChart.kt` | 实时图表和 CO2Data chunk 写入。 |
| `app/src/main/java/com/wldmedical/capnoeasy/pages/HistoryRecordDetailActivity.kt` | 历史波形读取、PDF 导出和热敏打印调用点。 |
| `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyApplication.kt` | App 启动时数据库与备份 helper 初始化。 |
| `app/src/main/java/com/wldmedical/capnoeasy/CapnoEasyConstant.kt` | 数据库/偏好 key、范围和设置枚举。 |
| `app/data_version_list.txt` | v1/v2 数据模型说明。 |
| `app/schemas/com.wldmedical.capnoeasy.kits.AppDatabase/1.json` | 现有 Room schema 快照。 |

## 全局注意事项

| 事项 | 代码事实 | 最小验证方式 |
| --- | --- | --- |
| Room 代码版本与 schema 快照不同步 | `AppDatabase` 当前 `version = 2` 且包含 `CO2Data`，但 `app/schemas/.../1.json` 仍是 version 1 快照。 | 检查 `LocalStorageKit.kt` 的 `@Database(version = 2)` 和 schema JSON 的 `database.version = 1`。 |
| 实时波形 chunk 大小 | 主记录链路在 `EtCo2LineChart` 使用 `maxRecordDataChunkSize = 100`；`LocalStorageKit.insertCO2DataForRecord` 内部另有本地 `chunkSize = 6000`，但该值没有参与当前主要实时写入。 | 检查 `EtCo2LineChart` 的 collect 写入和 `LocalStorageKit.insertCO2DataForRecord`。 |
| `handleSofrWareVersion` 拼写 | 函数名当前拼写为 `handleSofrWareVersion`，定位表与代码一致。 | `rg "handleSofrWareVersion"`。 |
| `GENDER.FORMALE` 拼写 | 性别枚举女为 `FORMALE`，代码多处依赖该名字。 | `rg "FORMALE"`。 |
| `WF_SPEED` 设备下发 | `updateCO2UnitScale` 接收 `wfSpeed` 参数但当前没有使用它下发命令。 | 检查 `BlueToothKit.updateCO2UnitScale`。 |
| 报警范围判断 | `handleCO2Waveform` 中 ETCO2/RR 的 `isValid*` 判断使用 `<= start && >= endInclusive`，按正常范围语义看不可同时满足；文档只记录当前行为，不改代码。 | 检查 `BlueToothKit.handleCO2Waveform`。 |

## 实体文档索引

| 序号 | 章节 | 实体 | 文档 | ID / 别名 |
| --- | --- | --- | --- | --- |
| 001 | 核心功能与业务流程 | BLE search flow | [001-ble-search-flow.md](core-data-protocol/001-ble-search-flow.md) | search devices, 扫描设备, 附近设备 |
| 002 | 核心功能与业务流程 | BLE connection bootstrap flow | [002-ble-connection-bootstrap-flow.md](core-data-protocol/002-ble-connection-bootstrap-flow.md) | connect device, GATT, 反劫持, 初始化连接 |
| 003 | 核心功能与业务流程 | Realtime CO2 ingestion flow | [003-realtime-co2-ingestion-flow.md](core-data-protocol/003-realtime-co2-ingestion-flow.md) | dataFlow, 波形数据流, 实时监测 |
| 004 | 核心功能与业务流程 | Alarm evaluation flow | [004-alarm-evaluation-flow.md](core-data-protocol/004-alarm-evaluation-flow.md) | alert audio, 报警判断, 报警音 |
| 005 | 核心功能与业务流程 | Recording lifecycle flow | [005-recording-lifecycle-flow.md](core-data-protocol/005-recording-lifecycle-flow.md) | start record, stop record, 记录开始停止 |
| 006 | 核心功能与业务流程 | Chunked waveform persistence flow | [006-chunked-waveform-persistence-flow.md](core-data-protocol/006-chunked-waveform-persistence-flow.md) | chunk storage, GZIP, trendData, 分块存储 |
| 007 | 核心功能与业务流程 | PDF export flow | [007-pdf-export-flow.md](core-data-protocol/007-pdf-export-flow.md) | save PDF, PDF 报告导出 |
| 008 | 核心功能与业务流程 | Thermal print flow | [008-thermal-print-flow.md](core-data-protocol/008-thermal-print-flow.md) | hotmelt print, ticket, 热敏打印 |
| 009 | 核心功能与业务流程 | Database backup and restore flow | [009-database-backup-and-restore-flow.md](core-data-protocol/009-database-backup-and-restore-flow.md) | db backup, restore, 数据库备份恢复 |
| 010 | 核心功能与业务流程 | Print logo image storage flow | [010-print-logo-image-storage-flow.md](core-data-protocol/010-print-logo-image-storage-flow.md) | print_logo.jpg, 图片选择, 内部图片 |
| 011 | Kit 与服务 | BlueToothKit | [011-bluetoothkit.md](core-data-protocol/011-bluetoothkit.md) | BLE核心, 蓝牙, 扫描, 连接, GATT |
| 012 | Kit 与服务 | BlueToothKitManager | [012-bluetoothkitmanager.md](core-data-protocol/012-bluetoothkitmanager.md) | bluetooth manager singleton, 蓝牙管理单例 |
| 013 | Kit 与服务 | BluetoothTaskQueue | [013-bluetoothtaskqueue.md](core-data-protocol/013-bluetoothtaskqueue.md) | BLE write queue, 写队列 |
| 014 | Kit 与服务 | CapnoEasyProtocalKit | [014-capnoeasyprotocalkit.md](core-data-protocol/014-capnoeasyprotocalkit.md) | protocol, 协议, UUID, 指令 |
| 015 | Kit 与服务 | LocalStorageKit | [015-localstoragekit.md](core-data-protocol/015-localstoragekit.md) | local storage, Room, SharedPreferences, 本地存储 |
| 016 | Kit 与服务 | LocalStorageKitManager | [016-localstoragekitmanager.md](core-data-protocol/016-localstoragekitmanager.md) | local storage manager, 本地存储管理单例 |
| 017 | Kit 与服务 | PDFKit | [017-pdfkit.md](core-data-protocol/017-pdfkit.md) | PDF, 报告导出 |
| 018 | Kit 与服务 | SaveChartToPdfTask | [018-savecharttopdftask.md](core-data-protocol/018-savecharttopdftask.md) | chart to PDF task, 图表导出 PDF 任务 |
| 019 | Kit 与服务 | PrintProtocalKitManager | [019-printprotocalkitmanager.md](core-data-protocol/019-printprotocalkitmanager.md) | print protocol, 打印协议 |
| 020 | Kit 与服务 | AlertAudioKit | [020-alertaudiokit.md](core-data-protocol/020-alertaudiokit.md) | alert sound, 报警音频 |
| 021 | Kit 与服务 | AudioPlayer | [021-audioplayer.md](core-data-protocol/021-audioplayer.md) | audio player, 音频播放器 |
| 022 | Kit 与服务 | ImageSelectorKit | [022-imageselectorkit.md](core-data-protocol/022-imageselectorkit.md) | image picker, 图片选择 |
| 023 | Kit 与服务 | DatabaseBackupHelper | [023-databasebackuphelper.md](core-data-protocol/023-databasebackuphelper.md) | database backup, 数据库备份, 恢复 |
| 024 | Kit 与服务 | DatabaseBackupHelperManager | [024-databasebackuphelpermanager.md](core-data-protocol/024-databasebackuphelpermanager.md) | backup manager, 备份管理单例 |
| 025 | Kit 与服务 | BluetoothDemoData | [025-bluetoothdemodata.md](core-data-protocol/025-bluetoothdemodata.md) | demo data, 演示数据 |
| 026 | Kit 与服务 | HotmeltPinter | [026-hotmeltpinter.md](core-data-protocol/026-hotmeltpinter.md) | thermal printer, 热敏打印 |
| 027 | Kit 与服务 | Printer | [027-printer.md](core-data-protocol/027-printer.md) | printer object, 打印机对象 |
| 028 | Kit 与服务 | PrintSetting | [028-printsetting.md](core-data-protocol/028-printsetting.md) | print settings, 打印设置 |
| 029 | 核心函数与方法 | BlueToothKit.searchDevices | [029-bluetoothkit-searchdevices.md](core-data-protocol/029-bluetoothkit-searchdevices.md) | scan devices, 扫描设备函数 |
| 030 | 核心函数与方法 | BlueToothKit.connectDevice | [030-bluetoothkit-connectdevice.md](core-data-protocol/030-bluetoothkit-connectdevice.md) | connect CapnoEasy, 连接设备函数 |
| 031 | 核心函数与方法 | BlueToothKit.initCapnoEasyConection | [031-bluetoothkit-initcapnoeasyconection.md](core-data-protocol/031-bluetoothkit-initcapnoeasyconection.md) | init device commands, 初始化设备连接 |
| 032 | 核心函数与方法 | BlueToothKit.fetchDeviceInfo | [032-bluetoothkit-fetchdeviceinfo.md](core-data-protocol/032-bluetoothkit-fetchdeviceinfo.md) | poll device info, 轮询设备信息 |
| 033 | 核心函数与方法 | BlueToothKit.updateReceivedData | [033-bluetoothkit-updatereceiveddata.md](core-data-protocol/033-bluetoothkit-updatereceiveddata.md) | update dataFlow, 更新实时数据流 |
| 034 | 核心函数与方法 | BlueToothKit.sendSavedData | [034-bluetoothkit-sendsaveddata.md](core-data-protocol/034-bluetoothkit-sendsaveddata.md) | send command packet, 发送命令包 |
| 035 | 核心函数与方法 | BlueToothKit.shutdown | [035-bluetoothkit-shutdown.md](core-data-protocol/035-bluetoothkit-shutdown.md) | reset command, 关机/复位 |
| 036 | 核心函数与方法 | BlueToothKit.correctZero | [036-bluetoothkit-correctzero.md](core-data-protocol/036-bluetoothkit-correctzero.md) | zero command, 校零函数 |
| 037 | 核心函数与方法 | BlueToothKit.updateCO2UnitScale | [037-bluetoothkit-updateco2unitscale.md](core-data-protocol/037-bluetoothkit-updateco2unitscale.md) | update display config, 更新单位量程 |
| 038 | 核心函数与方法 | BlueToothKit.updateCO2Unit | [038-bluetoothkit-updateco2unit.md](core-data-protocol/038-bluetoothkit-updateco2unit.md) | set CO2 unit, 设置单位 |
| 039 | 核心函数与方法 | BlueToothKit.updateCO2Scale | [039-bluetoothkit-updateco2scale.md](core-data-protocol/039-bluetoothkit-updateco2scale.md) | set CO2 scale, 设置量程 |
| 040 | 核心函数与方法 | BlueToothKit.updateAlertRange | [040-bluetoothkit-updatealertrange.md](core-data-protocol/040-bluetoothkit-updatealertrange.md) | set alert range, 设置报警范围 |
| 041 | 核心函数与方法 | BlueToothKit.innerUpdateAlertRange | [041-bluetoothkit-innerupdatealertrange.md](core-data-protocol/041-bluetoothkit-innerupdatealertrange.md) | build alert packet, 构造报警指令 |
| 042 | 核心函数与方法 | BlueToothKit.updateNoBreathAndCompensation | [042-bluetoothkit-updatenobreathandcompensation.md](core-data-protocol/042-bluetoothkit-updatenobreathandcompensation.md) | set module params, 设置模块参数 |
| 043 | 核心函数与方法 | BlueToothKit.updateNoBreath | [043-bluetoothkit-updatenobreath.md](core-data-protocol/043-bluetoothkit-updatenobreath.md) | set no breath, 设置窒息时间 |
| 044 | 核心函数与方法 | BlueToothKit.updateGasCompensation | [044-bluetoothkit-updategascompensation.md](core-data-protocol/044-bluetoothkit-updategascompensation.md) | set gas compensation, 设置气体补偿 |
| 045 | 核心函数与方法 | BlueToothKit.getCMDDataArray | [045-bluetoothkit-getcmddataarray.md](core-data-protocol/045-bluetoothkit-getcmddataarray.md) | frame extraction, 提取协议帧 |
| 046 | 核心函数与方法 | BlueToothKit.getSpecificValue | [046-bluetoothkit-getspecificvalue.md](core-data-protocol/046-bluetoothkit-getspecificvalue.md) | parse protocol frame, 解析协议帧 |
| 047 | 核心函数与方法 | BlueToothKit.handleCO2Waveform | [047-bluetoothkit-handleco2waveform.md](core-data-protocol/047-bluetoothkit-handleco2waveform.md) | parse 80H waveform, 解析波形 |
| 048 | 核心函数与方法 | BlueToothKit.handleCO2Status | [048-bluetoothkit-handleco2status.md](core-data-protocol/048-bluetoothkit-handleco2status.md) | parse CO2 status, 解析 CO2 状态 |
| 049 | 核心函数与方法 | BlueToothKit.handleSettings | [049-bluetoothkit-handlesettings.md](core-data-protocol/049-bluetoothkit-handlesettings.md) | parse 84H settings, 解析设置响应 |
| 050 | 核心函数与方法 | BlueToothKit.handleSofrWareVersion | [050-bluetoothkit-handlesofrwareversion.md](core-data-protocol/050-bluetoothkit-handlesofrwareversion.md) | parse CAH software, 解析软件版本 |
| 051 | 核心函数与方法 | BlueToothKit.handleSystemExpand | [051-bluetoothkit-handlesystemexpand.md](core-data-protocol/051-bluetoothkit-handlesystemexpand.md) | parse F2H expand, 解析扩展响应 |
| 052 | 核心函数与方法 | BlueToothKit.savePairedBLEDevice | [052-bluetoothkit-savepairedbledevice.md](core-data-protocol/052-bluetoothkit-savepairedbledevice.md) | save paired device, 保存配对设备 |
| 053 | 核心函数与方法 | BlueToothKit.getSavedBLEDeviceAddress | [053-bluetoothkit-getsavedbledeviceaddress.md](core-data-protocol/053-bluetoothkit-getsavedbledeviceaddress.md) | load paired device, 读取配对地址 |
| 054 | 核心函数与方法 | BlueToothKit.autoConnectPrinter | [054-bluetoothkit-autoconnectprinter.md](core-data-protocol/054-bluetoothkit-autoconnectprinter.md) | auto connect GP printer, 自动连接打印机 |
| 055 | 核心函数与方法 | BlueToothKit.isGPPrinterName | [055-bluetoothkit-isgpprintername.md](core-data-protocol/055-bluetoothkit-isgpprintername.md) | GP printer name match, 佳博打印机识别 |
| 056 | 核心函数与方法 | BlueToothKitManager.initialize | [056-bluetoothkitmanager-initialize.md](core-data-protocol/056-bluetoothkitmanager-initialize.md) | bluetooth singleton init, 蓝牙单例初始化 |
| 057 | 核心函数与方法 | BluetoothTaskQueue.addTask | [057-bluetoothtaskqueue-addtask.md](core-data-protocol/057-bluetoothtaskqueue-addtask.md) | enqueue task, 添加单任务 |
| 058 | 核心函数与方法 | BluetoothTaskQueue.addTasks | [058-bluetoothtaskqueue-addtasks.md](core-data-protocol/058-bluetoothtaskqueue-addtasks.md) | enqueue tasks, 添加多任务 |
| 059 | 核心函数与方法 | BluetoothTaskQueue.executeTask | [059-bluetoothtaskqueue-executetask.md](core-data-protocol/059-bluetoothtaskqueue-executetask.md) | execute one task, 执行单任务 |
| 060 | 核心函数与方法 | BluetoothTaskQueue.executeAllTasks | [060-bluetoothtaskqueue-executealltasks.md](core-data-protocol/060-bluetoothtaskqueue-executealltasks.md) | drain queue, 执行全部任务 |
| 061 | 核心函数与方法 | convert16BitUUIDto128Bit | [061-convert16bituuidto128bit.md](core-data-protocol/061-convert16bituuidto128bit.md) | UUID conversion, 16转128 UUID |
| 062 | 核心函数与方法 | List<CO2WavePointData>.compress | [062-list-co2wavepointdata-compress.md](core-data-protocol/062-list-co2wavepointdata-compress.md) | GZIP compress, 波形压缩 |
| 063 | 核心函数与方法 | ByteArray.decompressToCO2WavePointData | [063-bytearray-decompresstoco2wavepointdata.md](core-data-protocol/063-bytearray-decompresstoco2wavepointdata.md) | GZIP decompress, 波形解压 |
| 064 | 核心函数与方法 | AppDatabase.getDatabase | [064-appdatabase-getdatabase.md](core-data-protocol/064-appdatabase-getdatabase.md) | Room singleton, 获取数据库 |
| 065 | 核心函数与方法 | AppDatabase.clearInstance | [065-appdatabase-clearinstance.md](core-data-protocol/065-appdatabase-clearinstance.md) | clear DB singleton, 清理数据库单例 |
| 066 | 核心函数与方法 | MIGRATION_1_2.migrate | [066-migration-1-2-migrate.md](core-data-protocol/066-migration-1-2-migrate.md) | Room migration 1 to 2, 数据库迁移函数 |
| 067 | 核心函数与方法 | LocalStorageKit.savePatient | [067-localstoragekit-savepatient.md](core-data-protocol/067-localstoragekit-savepatient.md) | insert patient, 保存患者 |
| 068 | 核心函数与方法 | LocalStorageKit.saveRecord | [068-localstoragekit-saverecord.md](core-data-protocol/068-localstoragekit-saverecord.md) | insert record, 保存记录 |
| 069 | 核心函数与方法 | LocalStorageKit.stopRecord | [069-localstoragekit-stoprecord.md](core-data-protocol/069-localstoragekit-stoprecord.md) | finish record, 停止记录 |
| 070 | 核心函数与方法 | LocalStorageKit.insertCO2DataForRecord | [070-localstoragekit-insertco2dataforrecord.md](core-data-protocol/070-localstoragekit-insertco2dataforrecord.md) | insert waveform chunks, 写入波形块 |
| 071 | 核心函数与方法 | LocalStorageKit.getCO2DataForRecord | [071-localstoragekit-getco2dataforrecord.md](core-data-protocol/071-localstoragekit-getco2dataforrecord.md) | read waveform flow, 流式读取波形 |
| 072 | 核心函数与方法 | LocalStorageKit.getCO2DataForRecordPaged | [072-localstoragekit-getco2dataforrecordpaged.md](core-data-protocol/072-localstoragekit-getco2dataforrecordpaged.md) | paged waveform read, 分页读取波形 |
| 073 | 核心函数与方法 | LocalStorageKit.saveUserLanguageToPreferences | [073-localstoragekit-saveuserlanguagetopreferences.md](core-data-protocol/073-localstoragekit-saveuserlanguagetopreferences.md) | save language pref, 保存语言偏好 |
| 074 | 核心函数与方法 | LocalStorageKit.loadUserLanguageFromPreferences | [074-localstoragekit-loaduserlanguagefrompreferences.md](core-data-protocol/074-localstoragekit-loaduserlanguagefrompreferences.md) | load language pref, 读取语言偏好 |
| 075 | 核心函数与方法 | LocalStorageKit.saveUserPrintSettingToPreferences | [075-localstoragekit-saveuserprintsettingtopreferences.md](core-data-protocol/075-localstoragekit-saveuserprintsettingtopreferences.md) | save print prefs, 保存打印偏好 |
| 076 | 核心函数与方法 | LocalStorageKit.loadPrintSettingFromPreferences | [076-localstoragekit-loadprintsettingfrompreferences.md](core-data-protocol/076-localstoragekit-loadprintsettingfrompreferences.md) | load print prefs, 读取打印偏好 |
| 077 | 核心函数与方法 | LocalStorageKitManager.initialize | [077-localstoragekitmanager-initialize.md](core-data-protocol/077-localstoragekitmanager-initialize.md) | storage singleton init, 存储单例初始化 |
| 078 | 核心函数与方法 | filterData | [078-filterdata.md](core-data-protocol/078-filterdata.md) | waveform filter, 波形过滤 |
| 079 | 核心函数与方法 | saveChartToPdfInBackground | [079-savecharttopdfinbackground.md](core-data-protocol/079-savecharttopdfinbackground.md) | PDF task entry, PDF 任务入口 |
| 080 | 核心函数与方法 | SaveChartToPdfTask.savePDF | [080-savecharttopdftask-savepdf.md](core-data-protocol/080-savecharttopdftask-savepdf.md) | PDF render pipeline, PDF 生成流程 |
| 081 | 核心函数与方法 | SaveChartToPdfTask.addWaveformSections | [081-savecharttopdftask-addwaveformsections.md](core-data-protocol/081-savecharttopdftask-addwaveformsections.md) | report waveform sections, PDF 报告波形段 |
| 082 | 核心函数与方法 | SaveChartToPdfTask.createWaveformBitmap | [082-savecharttopdftask-createwaveformbitmap.md](core-data-protocol/082-savecharttopdftask-createwaveformbitmap.md) | manual waveform bitmap, PDF 手绘波形网格 |
| 083 | 核心函数与方法 | SaveChartToPdfTask.addWaveformMetrics | [083-savecharttopdftask-addwaveformmetrics.md](core-data-protocol/083-savecharttopdftask-addwaveformmetrics.md) | waveform metrics row, PDF 波形指标行 |
| 084 | 核心函数与方法 | AudioPlayer.playAlertAudio | [084-audioplayer-playalertaudio.md](core-data-protocol/084-audioplayer-playalertaudio.md) | play alert, 播放报警音 |
| 085 | 核心函数与方法 | AudioPlayer.stopAudio | [085-audioplayer-stopaudio.md](core-data-protocol/085-audioplayer-stopaudio.md) | stop alert, 停止报警音 |
| 086 | 核心函数与方法 | saveImageToInternalStorage | [086-saveimagetointernalstorage.md](core-data-protocol/086-saveimagetointernalstorage.md) | save print image, 保存内部图片 |
| 087 | 核心函数与方法 | loadImageFromInternalStorage | [087-loadimagefrominternalstorage.md](core-data-protocol/087-loadimagefrominternalstorage.md) | load print image, 读取内部图片 |
| 088 | 核心函数与方法 | DatabaseBackupHelper.startWork | [088-databasebackuphelper-startwork.md](core-data-protocol/088-databasebackuphelper-startwork.md) | backup worker entry, 备份恢复入口 |
| 089 | 核心函数与方法 | DatabaseBackupHelper.backupDatabase | [089-databasebackuphelper-backupdatabase.md](core-data-protocol/089-databasebackuphelper-backupdatabase.md) | backup DB files, 备份数据库文件 |
| 090 | 核心函数与方法 | DatabaseBackupHelper.backupDBFile | [090-databasebackuphelper-backupdbfile.md](core-data-protocol/090-databasebackuphelper-backupdbfile.md) | backup db file, 备份 DB |
| 091 | 核心函数与方法 | DatabaseBackupHelper.backupWALFile | [091-databasebackuphelper-backupwalfile.md](core-data-protocol/091-databasebackuphelper-backupwalfile.md) | backup WAL, 备份 WAL |
| 092 | 核心函数与方法 | DatabaseBackupHelper.backupSHMFile | [092-databasebackuphelper-backupshmfile.md](core-data-protocol/092-databasebackuphelper-backupshmfile.md) | backup SHM, 备份 SHM |
| 093 | 核心函数与方法 | DatabaseBackupHelper.restoreDatabase | [093-databasebackuphelper-restoredatabase.md](core-data-protocol/093-databasebackuphelper-restoredatabase.md) | restore DB, 恢复数据库 |
| 094 | 核心函数与方法 | DatabaseBackupHelperManager.initialize | [094-databasebackuphelpermanager-initialize.md](core-data-protocol/094-databasebackuphelpermanager-initialize.md) | backup singleton init, 备份单例初始化 |
| 095 | 核心函数与方法 | PrintProtocalKitManager.initialize | [095-printprotocalkitmanager-initialize.md](core-data-protocol/095-printprotocalkitmanager-initialize.md) | printer protocol init, 打印协议初始化 |
| 096 | 核心函数与方法 | Printer.connect | [096-printer-connect.md](core-data-protocol/096-printer-connect.md) | SDK printer connect, 连接打印机 SDK |
| 097 | 核心函数与方法 | Printer.getConnectState | [097-printer-getconnectstate.md](core-data-protocol/097-printer-getconnectstate.md) | SDK printer state, 打印机连接状态 |
| 098 | 核心函数与方法 | Printer.sendDataToPrinter | [098-printer-senddatatoprinter.md](core-data-protocol/098-printer-senddatatoprinter.md) | send ESC bytes, 发送打印指令 |
| 099 | 核心函数与方法 | Printer.close | [099-printer-close.md](core-data-protocol/099-printer-close.md) | close printer port, 关闭打印端口 |
| 100 | 核心函数与方法 | getCurrentFormattedDateTime | [100-getcurrentformatteddatetime.md](core-data-protocol/100-getcurrentformatteddatetime.md) | print timestamp, 打印时间格式化 |
| 101 | 核心函数与方法 | HotmeltPinter.connect | [101-hotmeltpinter-connect.md](core-data-protocol/101-hotmeltpinter-connect.md) | connect printer by mac, MAC 连接打印机 |
| 102 | 核心函数与方法 | HotmeltPinter.getConnectState | [102-hotmeltpinter-getconnectstate.md](core-data-protocol/102-hotmeltpinter-getconnectstate.md) | printer connection state, 热敏打印连接状态 |
| 103 | 核心函数与方法 | HotmeltPinter.print | [103-hotmeltpinter-print.md](core-data-protocol/103-hotmeltpinter-print.md) | print report ticket, 打印报告小票 |
| 104 | 核心函数与方法 | HotmeltPinter.generateWaveformBitmapNew | [104-hotmeltpinter-generatewaveformbitmapnew.md](core-data-protocol/104-hotmeltpinter-generatewaveformbitmapnew.md) | printer waveform bitmap, 打印波形 Bitmap |
| 105 | 核心函数与方法 | HotmeltPinter.startProcessingData | [105-hotmeltpinter-startprocessingdata.md](core-data-protocol/105-hotmeltpinter-startprocessingdata.md) | process print waveform, 打印波形处理 |
| 106 | 核心函数与方法 | HotmeltPinter.compressZeroSegments | [106-hotmeltpinter-compresszerosegments.md](core-data-protocol/106-hotmeltpinter-compresszerosegments.md) | compress zero waveform, 压缩零值波形段 |
| 107 | 核心函数与方法 | HotmeltPinter.loadScaledBitmap | [107-hotmeltpinter-loadscaledbitmap.md](core-data-protocol/107-hotmeltpinter-loadscaledbitmap.md) | load scaled bitmap, 打印图片缩放 |
| 108 | 存储与数据库 ID | DATABASE_NS | [108-database-ns.md](core-data-protocol/108-database-ns.md) | `wld_medical_capnoeasy_database` |
| 109 | 存储与数据库 ID | USER_PREF_NS | [109-user-pref-ns.md](core-data-protocol/109-user-pref-ns.md) | `wld_medical_capnoeasy_prefs` |
| 110 | 存储与数据库 ID | PAIRED_DEVICE_KEY | [110-paired-device-key.md](core-data-protocol/110-paired-device-key.md) | `paired_device_address` |
| 111 | 存储与数据库 ID | AppDatabase | [111-appdatabase.md](core-data-protocol/111-appdatabase.md) | Room database, schema v2, Room 数据库 |
| 112 | 存储与数据库 ID | Patient | [112-patient.md](core-data-protocol/112-patient.md) | `patients` table, 患者 |
| 113 | 存储与数据库 ID | Record | [113-record.md](core-data-protocol/113-record.md) | `records` table, 记录 |
| 114 | 存储与数据库 ID | CO2Data | [114-co2data.md](core-data-protocol/114-co2data.md) | `co2_data` table, CO2数据块 |
| 115 | 存储与数据库 ID | PatientDao | [115-patientdao.md](core-data-protocol/115-patientdao.md) | patients DAO, 患者 DAO |
| 116 | 存储与数据库 ID | RecordDao | [116-recorddao.md](core-data-protocol/116-recorddao.md) | records DAO, 记录 DAO |
| 117 | 存储与数据库 ID | CO2DataDao | [117-co2datadao.md](core-data-protocol/117-co2datadao.md) | co2_data DAO, CO2 数据 DAO |
| 118 | 存储与数据库 ID | MIGRATION_1_2 | [118-migration-1-2.md](core-data-protocol/118-migration-1-2.md) | Room migration 1 to 2, 数据库迁移 |
| 119 | 存储与数据库 ID | maxRecordDataChunkSize | [119-maxrecorddatachunksize.md](core-data-protocol/119-maxrecorddatachunksize.md) | chunk size, 数据块大小 |
| 120 | 存储与数据库 ID | trendStep | [120-trendstep.md](core-data-protocol/120-trendstep.md) | trend data step, 趋势步长 |
| 121 | BLE 协议 ID | SensorCommand.CO2Waveform | [121-sensorcommand-co2waveform.md](core-data-protocol/121-sensorcommand-co2waveform.md) | `0x80`, waveform, 波形数据 |
| 122 | BLE 协议 ID | SensorCommand.Zero | [122-sensorcommand-zero.md](core-data-protocol/122-sensorcommand-zero.md) | `0x82`, zero, 校零 |
| 123 | BLE 协议 ID | SensorCommand.Settings | [123-sensorcommand-settings.md](core-data-protocol/123-sensorcommand-settings.md) | `0x84`, settings, 设置 |
| 124 | BLE 协议 ID | SensorCommand.Expand | [124-sensorcommand-expand.md](core-data-protocol/124-sensorcommand-expand.md) | `0xF2`, expand, 扩展信息 |
| 125 | BLE 协议 ID | SensorCommand.NACKError | [125-sensorcommand-nackerror.md](core-data-protocol/125-sensorcommand-nackerror.md) | `0xC8`, NACK, 非应答错误 |
| 126 | BLE 协议 ID | SensorCommand.GetSoftwareRevision | [126-sensorcommand-getsoftwarerevision.md](core-data-protocol/126-sensorcommand-getsoftwarerevision.md) | `0xCA`, software revision, 软件版本 |
| 127 | BLE 协议 ID | SensorCommand.StopContinuous | [127-sensorcommand-stopcontinuous.md](core-data-protocol/127-sensorcommand-stopcontinuous.md) | `0xC9`, stop continuous, 停止连读 |
| 128 | BLE 协议 ID | SensorCommand.ResetNoBreaths | [128-sensorcommand-resetnobreaths.md](core-data-protocol/128-sensorcommand-resetnobreaths.md) | `0xCC`, reset no breaths, 清除窒息状态 |
| 129 | BLE 协议 ID | SensorCommand.Reset | [129-sensorcommand-reset.md](core-data-protocol/129-sensorcommand-reset.md) | `0xF8`, reset, 复位 |
| 130 | BLE 协议 ID | BLEServers.BLESendDataSer | [130-bleservers-blesenddataser.md](core-data-protocol/130-bleservers-blesenddataser.md) | `0xFFE5` |
| 131 | BLE 协议 ID | BLEServers.BLEReceiveDataSer | [131-bleservers-blereceivedataser.md](core-data-protocol/131-bleservers-blereceivedataser.md) | `0xFFE0` |
| 132 | BLE 协议 ID | BLEServers.BLEModuleParamsSer | [132-bleservers-blemoduleparamsser.md](core-data-protocol/132-bleservers-blemoduleparamsser.md) | `0xFF90` |
| 133 | BLE 协议 ID | BLEServers.BLEAntihijackSer | [133-bleservers-bleantihijackser.md](core-data-protocol/133-bleservers-bleantihijackser.md) | `0xFFC0`, anti-hijack, 反劫持 |
| 134 | BLE 协议 ID | BLECharacteristics.BLESendDataCha | [134-blecharacteristics-blesenddatacha.md](core-data-protocol/134-blecharacteristics-blesenddatacha.md) | `0xFFE9` |
| 135 | BLE 协议 ID | BLECharacteristics.BLEReceiveDataCha | [135-blecharacteristics-blereceivedatacha.md](core-data-protocol/135-blecharacteristics-blereceivedatacha.md) | `0xFFE4` |
| 136 | BLE 协议 ID | BLECharacteristics.BLERenameCha | [136-blecharacteristics-blerenamecha.md](core-data-protocol/136-blecharacteristics-blerenamecha.md) | `0xFF91` |
| 137 | BLE 协议 ID | BLECharacteristics.BLEBaudCha | [137-blecharacteristics-blebaudcha.md](core-data-protocol/137-blecharacteristics-blebaudcha.md) | `0xFF93` |
| 138 | BLE 协议 ID | BLECharacteristics.BLEAntihijackCha | [138-blecharacteristics-bleantihijackcha.md](core-data-protocol/138-blecharacteristics-bleantihijackcha.md) | `0xFFC1` |
| 139 | BLE 协议 ID | BLECharacteristics.BLEAntihijackChaNofi | [139-blecharacteristics-bleantihijackchanofi.md](core-data-protocol/139-blecharacteristics-bleantihijackchanofi.md) | `0xFFC2` |
| 140 | BLE 协议 ID | supportCMDs | [140-supportcmds.md](core-data-protocol/140-supportcmds.md) | supported commands, 支持的指令 |
| 141 | BLE 协议 ID | ZSBState | [141-zsbstate.md](core-data-protocol/141-zsbstate.md) | zero state, 校零状态 |
| 142 | BLE 协议 ID | ISBState80H | [142-isbstate80h.md](core-data-protocol/142-isbstate80h.md) | 80H data fields, 80H 数据字段 |
| 143 | BLE 协议 ID | ISBState84H | [143-isbstate84h.md](core-data-protocol/143-isbstate84h.md) | 84H settings fields, 84H 设置字段 |
| 144 | BLE 协议 ID | ISBStateF2H | [144-isbstatef2h.md](core-data-protocol/144-isbstatef2h.md) | F2H extension fields, F2H 扩展字段 |
| 145 | BLE 协议 ID | ISBStateCAH | [145-isbstatecah.md](core-data-protocol/145-isbstatecah.md) | CAH software info fields, CAH 软件信息字段 |
| 146 | 设置与常量 | CO2_UNIT | [146-co2-unit.md](core-data-protocol/146-co2-unit.md) | `MMHG`, `KPA`, `PERCENT`, CO2单位 |
| 147 | 设置与常量 | CO2_SCALE | [147-co2-scale.md](core-data-protocol/147-co2-scale.md) | waveform scale, CO2量程 |
| 148 | 设置与常量 | WF_SPEED | [148-wf-speed.md](core-data-protocol/148-wf-speed.md) | waveform speed, 扫描速度 |
| 149 | 设置与常量 | GENDER | [149-gender.md](core-data-protocol/149-gender.md) | MALE, FORMALE, 男, 女 |
| 150 | 设置与常量 | LanguageTypes | [150-languagetypes.md](core-data-protocol/150-languagetypes.md) | CHINESE, ENGLISH, 中文, English |
| 151 | 设置与常量 | BluetoothType | [151-bluetoothtype.md](core-data-protocol/151-bluetoothtype.md) | BLE, classic, 蓝牙类型 |
| 152 | 设置与常量 | AlertAudioType | [152-alertaudiotype.md](core-data-protocol/152-alertaudiotype.md) | low/middle alert, 报警声音 |
| 153 | 设置与常量 | GROUP_BY | [153-group-by.md](core-data-protocol/153-group-by.md) | ALL, PATIENT, DATE, 记录分组 |
| 154 | 设置与常量 | RangeType | [154-rangetype.md](core-data-protocol/154-rangetype.md) | range selector mode, 范围选择模式 |
| 155 | 设置与常量 | SettingType | [155-settingtype.md](core-data-protocol/155-settingtype.md) | setting row type, 设置行类型 |
| 156 | 设置与常量 | ToastType | [156-toasttype.md](core-data-protocol/156-toasttype.md) | toast type, 轻提示类型 |
| 157 | 设置与常量 | ETCO2Range | [157-etco2range.md](core-data-protocol/157-etco2range.md) | ETCO2 alert range, ETCO2 报警范围 |
| 158 | 设置与常量 | RRRange | [158-rrrange.md](core-data-protocol/158-rrrange.md) | RR alert range, RR 报警范围 |
| 159 | 设置与常量 | asphyxiationTimeRange | [159-asphyxiationtimerange.md](core-data-protocol/159-asphyxiationtimerange.md) | 窒息时间范围 |
| 160 | 设置与常量 | o2CompensationRange | [160-o2compensationrange.md](core-data-protocol/160-o2compensationrange.md) | O2补偿范围 |
| 161 | 设置与常量 | airPressureRange | [161-airpressurerange.md](core-data-protocol/161-airpressurerange.md) | 大气压范围 |
| 162 | 设置与常量 | patientAgeRange | [162-patientagerange.md](core-data-protocol/162-patientagerange.md) | 年龄范围 |

## 建议验证

| 验证 | 命令 / 方法 | 目的 |
| --- | --- | --- |
| 文档实体覆盖 | `find context/docs/core-data-protocol -name "*.md" | wc -l` | 确认任务实体文档数量为 162。 |
| 定位表链接 | `rg -n "context/docs/core-data-protocol/[0-9]{3}-" context/entity-id-mapping.md` | 确认任务实体能从定位表跳转到独立文档。 |
| 关键实体抽查 | `rg -n "BlueToothKit.searchDevices|SensorCommand.CO2Waveform|DATABASE_NS|patientAgeRange" context/docs/core-data-protocol context/entity-id-mapping.md` | 确认蓝牙、协议、存储和常量实体可检索。 |
| Kotlin 编译检查 | `./gradlew :app:compileDebugKotlin` | 本次只改 context，不强制运行；若业务代码随后改动再运行。 |
