package com.wldmedical.capnoeasy.kits

import com.wldmedical.hotmeltprint.HotmeltPinter

/**
 * 打印相关
 * 1、目前支持佳博打印机
 * 2、打印条形图
 * 3、打印相关设置
 * SDK地址
 * https://github.com/weisiwu/HotMeltPrintSDK
 */
object PrintProtocalKit {
    lateinit var printProtocalKit: HotmeltPinter

    fun initialize() {
        if (::printProtocalKit.isInitialized) {
            return
        }
        printProtocalKit = HotmeltPinter()
    }
}