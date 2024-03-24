package com.noemi.service.foreground

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.noemi.service.R

class ForegroundService : Service() {

    private lateinit var player: MediaPlayer
    private var position = 0

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer.create(this, R.raw.mj_who_is_it)
        createForeGroundNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {

                STOP_SERVICE -> {
                    stopSelf()
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }

                PLAY_MUSIC -> when (!player.isPlaying) {
                    true -> player.start()
                    else -> {
                        player.seekTo(position)
                        player.start()
                    }
                }

                PAUSE_MUSIC -> {
                    player.pause()
                    position = player.currentPosition
                }
            }
        }
        return START_NOT_STICKY
    }

    private fun createForeGroundNotification() {
        val remoteView = RemoteViews(packageName, R.layout.layout_play_music)

        val playIntent = getPlayIntent()
        remoteView.setImageViewResource(R.id.playIcon, R.drawable.ic_play)
        remoteView.setOnClickPendingIntent(R.id.playIcon, playIntent)

        val pauseIntent = getPauseIntent()
        remoteView.setImageViewResource(R.id.pauseIcon, R.drawable.ic_pause)
        remoteView.setOnClickPendingIntent(R.id.pauseIcon, pauseIntent)

        val stopIntent = getStopIntent()
        remoteView.setImageViewResource(R.id.stopIcon, R.drawable.ic_stop)
        remoteView.setOnClickPendingIntent(R.id.stopIcon, stopIntent)

        val notification = NotificationCompat.Builder(this, FOREGROUND_ID).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) foregroundServiceBehavior = NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE
            priority = NotificationCompat.PRIORITY_HIGH
            setSmallIcon(R.drawable.icon)
            setStyle(NotificationCompat.DecoratedCustomViewStyle())
            setCustomBigContentView(remoteView)
        }.build()

        notification.flags = NotificationCompat.FLAG_ONGOING_EVENT

        checkChannelAvailable()

        startForeground(21, notification)
    }

    private fun getPlayIntent(): PendingIntent {
        val play = Intent(this, ForegroundService::class.java)
        play.action = PLAY_MUSIC
        return PendingIntent.getService(this, 3, play, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getPauseIntent(): PendingIntent {
        val pause = Intent(this, ForegroundService::class.java)
        pause.action = PAUSE_MUSIC
        return PendingIntent.getService(this, 6, pause, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun getStopIntent(): PendingIntent {
        val stop = Intent(this, ForegroundService::class.java)
        stop.action = STOP_SERVICE
        return PendingIntent.getService(this, 9, stop, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
    }

    private fun checkChannelAvailable() {
        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val chanel = NotificationChannel(FOREGROUND_ID, FOREGROUND_TITLE, NotificationManager.IMPORTANCE_HIGH)
        manager.createNotificationChannel(chanel)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {

        private const val FOREGROUND_ID = "foreground_id"
        private const val FOREGROUND_TITLE = "foreground_title"
        private const val STOP_SERVICE = "stop_foreground_service"
        private const val PLAY_MUSIC = "play_music"
        private const val PAUSE_MUSIC = "pause_music"
    }
}