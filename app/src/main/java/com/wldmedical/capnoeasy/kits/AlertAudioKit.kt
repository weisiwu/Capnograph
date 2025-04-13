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
    private var isPlay: Boolean = false
    // 使用数字替代状态: 0: 不播放 1: 低级报警 2: 中级报警
    private var playStatus: Int = 0

    /**
     * 默认音频类型
     */
    fun playAlertAudio(type: AlertAudioType = AlertAudioType.MiddleLevelAlert) {
        val middleAlertResId = R.raw.middle_level_alert
        val lowAlertResId = R.raw.low_level_alert

        // 当前正在播放报警
        if (this.isPlay) {
//            println("wswTest【报警功能调试】 退出当前循环")
            return
        }

        // 播放新的报警前，先停止已有的
        stopAudio()

        // 没有音频在播放，进入准备播放期间
        playStatus = when (type) {
            AlertAudioType.MiddleLevelAlert -> 2
            AlertAudioType.LowLevelAlert -> 1
        }

        try {
            // 设置音频管理器
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            val moderateVolume = (maxVolume * 0.3).toInt()
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, moderateVolume, 0)

            // 如果是中级报警
            if (playStatus == 2) {
                mediaPlayer = MediaPlayer.create(context, middleAlertResId)
            }
            // 如果是低级报警
            else if (playStatus == 1) {
                mediaPlayer = MediaPlayer.create(context, lowAlertResId)
            }

            mediaPlayer?.start()
            this.isPlay = true

            // 播放14秒后，停止播放
            Handler(Looper.getMainLooper()).postDelayed({
                stopAudio()
                this.isPlay = false
            }, 14000)
        } catch (e: IOException) {
            stopAudio()
            println("wswTest【报警功能调试】无法播放音频文件: ${e.localizedMessage}")
        }
    }
    
    fun stopAudio() {
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