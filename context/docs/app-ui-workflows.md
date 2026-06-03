# 应用入口、页面、UI 与状态补充上下文索引

来源批次：应用入口、页面、UI 与状态实体补充。
定位入口：`context/entity-id-mapping.md`。

本文只作为第二组任务的索引；实体级补充上下文位于 `context/docs/app-ui-workflows/`，每个实体对应一个独立 Markdown 文档。

## 使用规则

- 修改页面、组件、状态或导航前，先在 `context/entity-id-mapping.md` 定位实体，再读取该实体行的 `补充上下文`。
- 每个实体的补充上下文只维护在对应的独立文档中；本文不承载实体细节。
- 文档和源码不一致时，以源码为准，并同步修正对应实体文档和定位表。

## 实体文档

| 序号 | 定位行 | 实体 | 领域 | 文档 |
| --- | --- | --- | --- | --- |
| 001 | L75 | CapnoEasyApplication | 应用入口 | `context/docs/app-ui-workflows/001-capnoeasyapplication.md` |
| 002 | L76 | SplashActivity | 页面 | `context/docs/app-ui-workflows/002-splashactivity.md` |
| 003 | L77 | BaseActivity | 页面 | `context/docs/app-ui-workflows/003-baseactivity.md` |
| 004 | L78 | MainActivity | 页面 | `context/docs/app-ui-workflows/004-mainactivity.md` |
| 005 | L79 | SearchActivity | 页面 | `context/docs/app-ui-workflows/005-searchactivity.md` |
| 006 | L80 | SettingActivity | 页面 | `context/docs/app-ui-workflows/006-settingactivity.md` |
| 007 | L81 | AlertSettingActivity | 页面 | `context/docs/app-ui-workflows/007-alertsettingactivity.md` |
| 008 | L82 | DisplaySettingActivity | 页面 | `context/docs/app-ui-workflows/008-displaysettingactivity.md` |
| 009 | L83 | ModuleSettingActivity | 页面 | `context/docs/app-ui-workflows/009-modulesettingactivity.md` |
| 010 | L84 | PrintSettingActivity | 页面 | `context/docs/app-ui-workflows/010-printsettingactivity.md` |
| 011 | L85 | SystemSettingActivity | 页面 | `context/docs/app-ui-workflows/011-systemsettingactivity.md` |
| 012 | L86 | HistoryRecordsActivity | 页面 | `context/docs/app-ui-workflows/012-historyrecordsactivity.md` |
| 013 | L87 | HistoryRecordDetailActivity | 页面 | `context/docs/app-ui-workflows/013-historyrecorddetailactivity.md` |
| 014 | L93 | PageScene.HOME_PAGE | 常量 | `context/docs/app-ui-workflows/014-pagescene-home-page.md` |
| 015 | L94 | PageScene.SETTING_PAGE | 常量 | `context/docs/app-ui-workflows/015-pagescene-setting-page.md` |
| 016 | L95 | PageScene.DEVICES_LIST_PAGE | 常量 | `context/docs/app-ui-workflows/016-pagescene-devices-list-page.md` |
| 017 | L96 | PageScene.SYSTEM_CONFIG_PAGE | 常量 | `context/docs/app-ui-workflows/017-pagescene-system-config-page.md` |
| 018 | L97 | PageScene.ALERT_CONFIG_PAGE | 常量 | `context/docs/app-ui-workflows/018-pagescene-alert-config-page.md` |
| 019 | L98 | PageScene.DISPLAY_CONFIG_PAGE | 常量 | `context/docs/app-ui-workflows/019-pagescene-display-config-page.md` |
| 020 | L99 | PageScene.MODULE_CONFIG_PAGE | 常量 | `context/docs/app-ui-workflows/020-pagescene-module-config-page.md` |
| 021 | L100 | PageScene.PRINT_CONFIG_PAGE | 常量 | `context/docs/app-ui-workflows/021-pagescene-print-config-page.md` |
| 022 | L101 | PageScene.HISTORY_LIST_PAGE | 常量 | `context/docs/app-ui-workflows/022-pagescene-history-list-page.md` |
| 023 | L102 | PageScene.HISTORY_DETAIL_PAGE | 常量 | `context/docs/app-ui-workflows/023-pagescene-history-detail-page.md` |
| 024 | L108 | App bootstrap flow | 应用入口 | `context/docs/app-ui-workflows/024-app-bootstrap-flow.md` |
| 025 | L109 | Page shell and overlays flow | UI 流程 | `context/docs/app-ui-workflows/025-page-shell-and-overlays-flow.md` |
| 026 | L116 | History records flow | 历史记录 | `context/docs/app-ui-workflows/026-history-records-flow.md` |
| 027 | L117 | History detail chart flow | 历史记录 | `context/docs/app-ui-workflows/027-history-detail-chart-flow.md` |
| 028 | L120 | Display settings flow | 设置 | `context/docs/app-ui-workflows/028-display-settings-flow.md` |
| 029 | L121 | Alert settings flow | 设置 | `context/docs/app-ui-workflows/029-alert-settings-flow.md` |
| 030 | L122 | Module settings flow | 设置 | `context/docs/app-ui-workflows/030-module-settings-flow.md` |
| 031 | L123 | Print preferences flow | 设置 | `context/docs/app-ui-workflows/031-print-preferences-flow.md` |
| 032 | L124 | System language and module info flow | 设置 | `context/docs/app-ui-workflows/032-system-language-and-module-info-flow.md` |
| 033 | L132 | AppState | 状态 | `context/docs/app-ui-workflows/033-appstate.md` |
| 034 | L133 | AppStateModel | 状态 | `context/docs/app-ui-workflows/034-appstatemodel.md` |
| 035 | L134 | CO2WavePointData | 数据模型 | `context/docs/app-ui-workflows/035-co2wavepointdata.md` |
| 036 | L135 | DataPoint | 数据模型 | `context/docs/app-ui-workflows/036-datapoint.md` |
| 037 | L136 | BLEDeviceExtra | 蓝牙 | `context/docs/app-ui-workflows/037-bledeviceextra.md` |
| 038 | L137 | Device | 组件 | `context/docs/app-ui-workflows/038-device.md` |
| 039 | L143 | `AppStateModel.updateCurrentPage` | 状态函数 | `context/docs/app-ui-workflows/039-appstatemodel-updatecurrentpage.md` |
| 040 | L144 | `AppStateModel.updateCurrentTab` | 状态函数 | `context/docs/app-ui-workflows/040-appstatemodel-updatecurrenttab.md` |
| 041 | L145 | `AppStateModel.updateToastData` | 状态函数 | `context/docs/app-ui-workflows/041-appstatemodel-updatetoastdata.md` |
| 042 | L146 | `AppStateModel.updateAlertData` | 状态函数 | `context/docs/app-ui-workflows/042-appstatemodel-updatealertdata.md` |
| 043 | L147 | `AppStateModel.updateConfirmData` | 状态函数 | `context/docs/app-ui-workflows/043-appstatemodel-updateconfirmdata.md` |
| 044 | L148 | `AppStateModel.updateLoadingData` | 状态函数 | `context/docs/app-ui-workflows/044-appstatemodel-updateloadingdata.md` |
| 045 | L149 | `AppStateModel.updateShowActionModal` | 状态函数 | `context/docs/app-ui-workflows/045-appstatemodel-updateshowactionmodal.md` |
| 046 | L150 | `AppStateModel.clearXData` | 状态函数 | `context/docs/app-ui-workflows/046-appstatemodel-clearxdata.md` |
| 047 | L151 | `AppStateModel.updateKeepScreenOn` | 状态函数 | `context/docs/app-ui-workflows/047-appstatemodel-updatekeepscreenon.md` |
| 048 | L152 | `AppStateModel.updateDevices` | 状态函数 | `context/docs/app-ui-workflows/048-appstatemodel-updatedevices.md` |
| 049 | L153 | `AppStateModel.updateConnectType` | 状态函数 | `context/docs/app-ui-workflows/049-appstatemodel-updateconnecttype.md` |
| 050 | L154 | `AppStateModel.updateIsRecording` | 状态函数 | `context/docs/app-ui-workflows/050-appstatemodel-updateisrecording.md` |
| 051 | L155 | `AppStateModel.updateTotalCO2WavedData` | 状态函数 | `context/docs/app-ui-workflows/051-appstatemodel-updatetotalco2waveddata.md` |
| 052 | L156 | `AppStateModel.delSavedCO2WavedDataChunk` | 状态函数 | `context/docs/app-ui-workflows/052-appstatemodel-delsavedco2waveddatachunk.md` |
| 053 | L157 | `AppStateModel.updateAlertETCO2Range` | 状态函数 | `context/docs/app-ui-workflows/053-appstatemodel-updatealertetco2range.md` |
| 054 | L158 | `AppStateModel.updateAlertRRRange` | 状态函数 | `context/docs/app-ui-workflows/054-appstatemodel-updatealertrrrange.md` |
| 055 | L159 | `AppStateModel.updateCO2Unit` | 状态函数 | `context/docs/app-ui-workflows/055-appstatemodel-updateco2unit.md` |
| 056 | L160 | `AppStateModel.updateCO2Scale` | 状态函数 | `context/docs/app-ui-workflows/056-appstatemodel-updateco2scale.md` |
| 057 | L161 | `AppStateModel.updateCo2Scales` | 状态函数 | `context/docs/app-ui-workflows/057-appstatemodel-updateco2scales.md` |
| 058 | L162 | `AppStateModel.updateWFSpeed` | 状态函数 | `context/docs/app-ui-workflows/058-appstatemodel-updatewfspeed.md` |
| 059 | L163 | `AppStateModel.updateAsphyxiationTime` | 状态函数 | `context/docs/app-ui-workflows/059-appstatemodel-updateasphyxiationtime.md` |
| 060 | L164 | `AppStateModel.updateO2Compensation` | 状态函数 | `context/docs/app-ui-workflows/060-appstatemodel-updateo2compensation.md` |
| 061 | L165 | `AppStateModel.updateAirPressure` | 状态函数 | `context/docs/app-ui-workflows/061-appstatemodel-updateairpressure.md` |
| 062 | L166 | `AppStateModel.updateLanguage` | 状态函数 | `context/docs/app-ui-workflows/062-appstatemodel-updatelanguage.md` |
| 063 | L167 | `AppStateModel.updateShowTrendingChart` | 状态函数 | `context/docs/app-ui-workflows/063-appstatemodel-updateshowtrendingchart.md` |
| 064 | L168 | `AppStateModel.updatePdfHospitalName` | 状态函数 | `context/docs/app-ui-workflows/064-appstatemodel-updatepdfhospitalname.md` |
| 065 | L169 | `AppStateModel.updatePdfReportName` | 状态函数 | `context/docs/app-ui-workflows/065-appstatemodel-updatepdfreportname.md` |
| 066 | L170 | `AppStateModel.updateIsPDF` | 状态函数 | `context/docs/app-ui-workflows/066-appstatemodel-updateispdf.md` |
| 067 | L171 | `AppStateModel.updatePatientName` | 状态函数 | `context/docs/app-ui-workflows/067-appstatemodel-updatepatientname.md` |
| 068 | L172 | `AppStateModel.updatePatientGender` | 状态函数 | `context/docs/app-ui-workflows/068-appstatemodel-updatepatientgender.md` |
| 069 | L173 | `AppStateModel.updatePatientAge` | 状态函数 | `context/docs/app-ui-workflows/069-appstatemodel-updatepatientage.md` |
| 070 | L174 | `AppStateModel.updatePatientID` | 状态函数 | `context/docs/app-ui-workflows/070-appstatemodel-updatepatientid.md` |
| 071 | L175 | `AppStateModel.updatePatientDepartment` | 状态函数 | `context/docs/app-ui-workflows/071-appstatemodel-updatepatientdepartment.md` |
| 072 | L176 | `AppStateModel.updatePatientBedNumber` | 状态函数 | `context/docs/app-ui-workflows/072-appstatemodel-updatepatientbednumber.md` |
| 073 | L177 | `AppStateModel.updateDiscoveredPeripherals` | 状态函数 | `context/docs/app-ui-workflows/073-appstatemodel-updatediscoveredperipherals.md` |
| 074 | L183 | BaseLayout | 组件 | `context/docs/app-ui-workflows/074-baselayout.md` |
| 075 | L184 | ActionBar | 组件 | `context/docs/app-ui-workflows/075-actionbar.md` |
| 076 | L185 | NavBar | 组件 | `context/docs/app-ui-workflows/076-navbar.md` |
| 077 | L186 | EtCo2LineChart | 组件 | `context/docs/app-ui-workflows/077-etco2linechart.md` |
| 078 | L187 | EtCo2Table | 组件 | `context/docs/app-ui-workflows/078-etco2table.md` |
| 079 | L188 | DeviceList | 组件 | `context/docs/app-ui-workflows/079-devicelist.md` |
| 080 | L189 | HistoryList | 组件 | `context/docs/app-ui-workflows/080-historylist.md` |
| 081 | L190 | SettingList | 组件 | `context/docs/app-ui-workflows/081-settinglist.md` |
| 082 | L191 | RangeSelector | 组件 | `context/docs/app-ui-workflows/082-rangeselector.md` |
| 083 | L192 | WheelPicker | 组件 | `context/docs/app-ui-workflows/083-wheelpicker.md` |
| 084 | L193 | TypeSwitch | 组件 | `context/docs/app-ui-workflows/084-typeswitch.md` |
| 085 | L194 | ActionModal | 组件 | `context/docs/app-ui-workflows/085-actionmodal.md` |
| 086 | L195 | AlertModal | 组件 | `context/docs/app-ui-workflows/086-alertmodal.md` |
| 087 | L196 | ConfirmModal | 组件 | `context/docs/app-ui-workflows/087-confirmmodal.md` |
| 088 | L197 | Toast | 组件 | `context/docs/app-ui-workflows/088-toast.md` |
| 089 | L198 | Loading | 组件 | `context/docs/app-ui-workflows/089-loading.md` |
| 090 | L199 | SaveButton | 组件 | `context/docs/app-ui-workflows/090-savebutton.md` |
| 091 | L200 | CustomTextField | 组件 | `context/docs/app-ui-workflows/091-customtextfield.md` |
| 092 | L206 | NavBarComponentState | 组件模型 | `context/docs/app-ui-workflows/092-navbarcomponentstate.md` |
| 093 | L207 | TabItem | 组件模型 | `context/docs/app-ui-workflows/093-tabitem.md` |
| 094 | L208 | Atribute | 组件模型 | `context/docs/app-ui-workflows/094-atribute.md` |
| 095 | L209 | Setting | 组件模型 | `context/docs/app-ui-workflows/095-setting.md` |
| 096 | L210 | AlertData | 组件模型 | `context/docs/app-ui-workflows/096-alertdata.md` |
| 097 | L211 | ConfirmData | 组件模型 | `context/docs/app-ui-workflows/097-confirmdata.md` |
| 098 | L212 | ToastData | 组件模型 | `context/docs/app-ui-workflows/098-toastdata.md` |
| 099 | L213 | LoadingData | 组件模型 | `context/docs/app-ui-workflows/099-loadingdata.md` |
| 100 | L214 | SupportQRCodeType | 组件模型 | `context/docs/app-ui-workflows/100-supportqrcodetype.md` |
| 101 | L215 | CustomType | 组件模型 | `context/docs/app-ui-workflows/101-customtype.md` |
| 102 | L216 | DeviceType | 组件模型 | `context/docs/app-ui-workflows/102-devicetype.md` |
| 103 | L217 | DeviceTypeList | 组件模型 | `context/docs/app-ui-workflows/103-devicetypelist.md` |
| 104 | L218 | OutputType | 组件模型 | `context/docs/app-ui-workflows/104-outputtype.md` |
| 105 | L247 | `CapnoEasyApplication.onCreate` | 应用入口 | `context/docs/app-ui-workflows/105-capnoeasyapplication-oncreate.md` |
| 106 | L248 | `CapnoEasyApplication.getActivityCount` | 应用入口 | `context/docs/app-ui-workflows/106-capnoeasyapplication-getactivitycount.md` |
| 107 | L249 | `SplashActivity.onCreate` | 页面函数 | `context/docs/app-ui-workflows/107-splashactivity-oncreate.md` |
| 108 | L250 | `SplashScreen` | UI 函数 | `context/docs/app-ui-workflows/108-splashscreen.md` |
| 109 | L251 | `BaseActivity.onCreate` | 页面函数 | `context/docs/app-ui-workflows/109-baseactivity-oncreate.md` |
| 110 | L252 | `BaseActivity.onBackPressed` | 页面函数 | `context/docs/app-ui-workflows/110-baseactivity-onbackpressed.md` |
| 111 | L253 | `BaseActivity.ShowLoading` | UI 函数 | `context/docs/app-ui-workflows/111-baseactivity-showloading.md` |
| 112 | L254 | `BaseActivity.ShowAlert` | UI 函数 | `context/docs/app-ui-workflows/112-baseactivity-showalert.md` |
| 113 | L255 | `BaseActivity.ShowConfirm` | UI 函数 | `context/docs/app-ui-workflows/113-baseactivity-showconfirm.md` |
| 114 | L256 | `BaseActivity.ShowToast` | UI 函数 | `context/docs/app-ui-workflows/114-baseactivity-showtoast.md` |
| 115 | L257 | `BaseActivity.ShowActionModal` | UI 函数 | `context/docs/app-ui-workflows/115-baseactivity-showactionmodal.md` |
| 116 | L258 | `BaseActivity.updatePageScene` | 页面函数 | `context/docs/app-ui-workflows/116-baseactivity-updatepagescene.md` |
| 117 | L259 | `BaseActivity.checkHasConnectDevice` | 页面函数 | `context/docs/app-ui-workflows/117-baseactivity-checkhasconnectdevice.md` |
| 118 | L260 | `BaseActivity.checkBluetoothPermissions` | 页面函数 | `context/docs/app-ui-workflows/118-baseactivity-checkbluetoothpermissions.md` |
| 119 | L261 | `BaseActivity.initializeBlueToothKit` | 页面函数 | `context/docs/app-ui-workflows/119-baseactivity-initializebluetoothkit.md` |
| 120 | L262 | `MainActivity.onCreate` | 页面函数 | `context/docs/app-ui-workflows/120-mainactivity-oncreate.md` |
| 121 | L263 | `MainActivity.onTabClick` | 页面函数 | `context/docs/app-ui-workflows/121-mainactivity-ontabclick.md` |
| 122 | L264 | `MainActivity.onNavBarRightClick` | 页面函数 | `context/docs/app-ui-workflows/122-mainactivity-onnavbarrightclick.md` |
| 123 | L265 | `MainActivity.onActivityResult` | 页面函数 | `context/docs/app-ui-workflows/123-mainactivity-onactivityresult.md` |
| 124 | L266 | `MainActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/124-mainactivity-content.md` |
| 125 | L267 | `SearchActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/125-searchactivity-content.md` |
| 126 | L268 | `DisplaySettingActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/126-displaysettingactivity-content.md` |
| 127 | L269 | `AlertSettingActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/127-alertsettingactivity-content.md` |
| 128 | L270 | `ModuleSettingActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/128-modulesettingactivity-content.md` |
| 129 | L271 | `PrintSettingActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/129-printsettingactivity-content.md` |
| 130 | L272 | `SystemSettingActivity.updateLanguage` | 页面函数 | `context/docs/app-ui-workflows/130-systemsettingactivity-updatelanguage.md` |
| 131 | L273 | `SystemSettingActivity.onCreate` | 页面函数 | `context/docs/app-ui-workflows/131-systemsettingactivity-oncreate.md` |
| 132 | L274 | `SystemSettingActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/132-systemsettingactivity-content.md` |
| 133 | L275 | `HistoryRecordsActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/133-historyrecordsactivity-content.md` |
| 134 | L276 | `HistoryRecordDetailActivity.onSavePDFClick` | 页面函数 | `context/docs/app-ui-workflows/134-historyrecorddetailactivity-onsavepdfclick.md` |
| 135 | L277 | `HistoryRecordDetailActivity.onPrintTicketClick` | 页面函数 | `context/docs/app-ui-workflows/135-historyrecorddetailactivity-onprintticketclick.md` |
| 136 | L278 | `HistoryRecordDetailActivity.loadAllCo2Data` | 页面函数 | `context/docs/app-ui-workflows/136-historyrecorddetailactivity-loadallco2data.md` |
| 137 | L279 | `HistoryRecordDetailActivity.createPdfDocument` | 页面函数 | `context/docs/app-ui-workflows/137-historyrecorddetailactivity-createpdfdocument.md` |
| 138 | L280 | `HistoryRecordDetailActivity.savePdfToUri` | 页面函数 | `context/docs/app-ui-workflows/138-historyrecorddetailactivity-savepdftouri.md` |
| 139 | L281 | `HistoryRecordDetailActivity.Content` | UI 函数 | `context/docs/app-ui-workflows/139-historyrecorddetailactivity-content.md` |
| 140 | L282 | `EtCo2LineChart` | UI 函数 | `context/docs/app-ui-workflows/140-etco2linechart-2.md` |
| 141 | L283 | `AttributeLine` | UI 函数 | `context/docs/app-ui-workflows/141-attributeline.md` |
| 142 | L284 | `EtCo2Table` | UI 函数 | `context/docs/app-ui-workflows/142-etco2table-2.md` |
| 143 | L285 | `HistoryList` | UI 函数 | `context/docs/app-ui-workflows/143-historylist-2.md` |
| 144 | L373 | patientParams | 导航 | `context/docs/app-ui-workflows/144-patientparams.md` |
| 145 | L374 | recordIdParams | 导航 | `context/docs/app-ui-workflows/145-recordidparams.md` |
| 146 | L171 | `AppStateModel.updatePdfTemplateMode` | 状态函数 | `context/docs/app-ui-workflows/146-appstatemodel-updatepdftemplatemode.md` |
| 147 | L172 | `AppStateModel.updatePdfWatermarkEnabled` | 状态函数 | `context/docs/app-ui-workflows/147-appstatemodel-updatepdfwatermarkenabled.md` |
| 148 | L173 | `AppStateModel.updatePdfWatermarkText` | 状态函数 | `context/docs/app-ui-workflows/148-appstatemodel-updatepdfwatermarktext.md` |
| 149 | L174 | `AppStateModel.updatePdfWatermarkOpacity` | 状态函数 | `context/docs/app-ui-workflows/149-appstatemodel-updatepdfwatermarkopacity.md` |
