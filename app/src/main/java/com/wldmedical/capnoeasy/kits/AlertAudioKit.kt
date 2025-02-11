package com.wldmedical.capnoeasy.kits

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.wldmedical.capnoeasy.R
import java.io.IOException

/**
 * 报警设置，播放报警和终止报警
 */

class AudioPlayer(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    private var isReady: Boolean = true
    // 使用数字替代状态: 0: 不播放 1: 低级报警 2: 中级报警
    private var playStatus: Int = 0

    /**
     * 默认音频类型
     */
//    fun playAlertAudio(type: AlertAudioType = AlertAudioType.MiddleLevelAlert) {
//        val middleAlertResId = R.raw.middle_level_alert
//        val lowAlertResId = R.raw.low_level_alert
//
//        if (!isReady) {
//            return
//        }
//
//        val newPlayStatus = if (type == AlertAudioType.MiddleLevelAlert) 2 else 1
//
//        // 如果新状态是不播放，直接退出
//        if (newPlayStatus == 0) {
//            playStatus = 0
//            // 播放新的报警前，先停止已有的
//            stopAudio()
//            return
//        }
//
//        try {
//            // 设置音频管理器
//            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
//            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0)
//
//            // 如果是中级报警
//            if (newPlayStatus == 2 && isReady) {
//                // 播放新的报警前，先停止已有的
//                stopAudio()
//                mediaPlayer = MediaPlayer.create(context, middleAlertResId)
//                mediaPlayer?.setOnCompletionListener {
//                    isReady = true
//                }
//                mediaPlayer?.start()
//                isReady = false
//                playStatus = newPlayStatus
//
//                // 设置延迟使其可以接受新的播放
//                Handler(Looper.getMainLooper()).postDelayed({ isReady = true }, 14000)
//            }
//            // 如果是低级报警
//            else if (newPlayStatus == 1 && isReady) {
//                // 播放新的报警前，先停止已有的
//                stopAudio()
//                mediaPlayer = MediaPlayer.create(context, lowAlertResId)
//                mediaPlayer?.setOnCompletionListener {
//                    isReady = true
//                }
//                mediaPlayer?.start()
//                isReady = false
//                playStatus = newPlayStatus
//
//                // 设置延迟使其可以接受新的播放
//                Handler(Looper.getMainLooper()).postDelayed({ isReady = true }, 14000)
//            }
//        } catch (e: IOException) {
//            println("无法播放音频文件: ${e.localizedMessage}")
//        }
//    }
    // TODO: 临时注释掉 
    fun playAlertAudio(type: AlertAudioType = AlertAudioType.MiddleLevelAlert) {
    }
    
    fun stopAudio() {
        isReady = true
        mediaPlayer?.stop()
        mediaPlayer?.release() // 释放资源
        mediaPlayer = null
    }
}

/**
 * 报警类型
 */
enum class AlertAudioType {
    // 低级报警
    // 技术报警: 需要零点校准/无适配器/适配器污染
    LowLevelAlert,
    // 中级报警
    // 生理报警: ETCO2值低/ETCO2值高/RR 值高/RR值低/窒息/电池电量低
    MiddleLevelAlert
}