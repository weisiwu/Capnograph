package com.wldmedical.capnoeasy.kits

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.wldmedical.capnoeasy.R
import io.github.weisiwu.slstracker.SlsLogTracker.Level

/**
 * 报警设置，播放报警和终止报警
 */

class AudioPlayer internal constructor(
    private val context: Context,
    private val createPlayer: (Context, Int) -> MediaPlayer?
) {
    constructor(context: Context) : this(context, MediaPlayer::create)

    private val handler = Handler(Looper.getMainLooper())
    private var scheduledStop: Runnable? = null
    private var mediaPlayer: MediaPlayer? = null

    @Synchronized
    fun playAlertAudio(type: AlertAudioType = AlertAudioType.MiddleLevelAlert) {
        if (mediaPlayer != null) return
        val sound = when (type) {
            AlertAudioType.MiddleLevelAlert -> R.raw.middle_level_alert
            AlertAudioType.LowLevelAlert -> R.raw.low_level_alert
        }
        try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (maxVolume * 0.3).toInt(), 0)

            val player = createPlayer(context, sound)
            if (player == null) {
                ErrorReporter.diagnostic(Level.ERROR, "AlertAudio.create_player_failed")
                return
            }
            mediaPlayer = player
            player.setOnErrorListener(::handlePlaybackError)
            player.start()
            val timeout = Runnable { stopIfCurrent(player) }
            scheduledStop = timeout
            handler.postDelayed(timeout, 14_000)
        } catch (e: Exception) {
            stopAudio()
            ErrorReporter.report(e, "AlertAudio.play_alert_audio", mapOf("alert_type" to type.name))
        }
    }

    @Synchronized
    internal fun handlePlaybackError(player: MediaPlayer, what: Int, extra: Int): Boolean {
        if (player === mediaPlayer) {
            ErrorReporter.diagnostic(Level.ERROR, "AlertAudio.playback_failed", what, detailCode = extra)
            stopAudio()
        }
        return true
    }

    @Synchronized
    private fun stopIfCurrent(player: MediaPlayer) {
        if (player === mediaPlayer) stopAudio()
    }

    @Synchronized
    fun stopAudio() {
        scheduledStop?.let(handler::removeCallbacks)
        scheduledStop = null
        val player = mediaPlayer ?: return
        mediaPlayer = null
        runCatching {
            player.setOnErrorListener(null)
            if (player.isPlaying) player.stop()
        }.onFailure {
            ErrorReporter.report(it, "AlertAudio.stop_audio")
        }
        runCatching {
            player.release()
        }.onFailure {
            ErrorReporter.report(it, "AlertAudio.release_audio")
        }
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
