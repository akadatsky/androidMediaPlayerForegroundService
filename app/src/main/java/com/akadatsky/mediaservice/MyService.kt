package com.akadatsky.mediaservice

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.AssetFileDescriptor
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


class MyService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var receiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        mediaPlayer = MediaPlayer.create(this, R.raw.sound_file)
//        mediaPlayerFromAssets()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        register()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun register() {
        val filter = IntentFilter()
        filter.addAction(MEDIA_ACTION)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val play: Boolean = intent.getBooleanExtra(PLAY, false)
                if (play) {
                    mediaPlayer?.start()
                } else {
                    mediaPlayer?.pause()
                }
                //        mediaPlayer.reset()
                //        mediaPlayer.setDataSource(MEDIA_PATH);
                //        mediaPlayer.seekTo(0)
                //        mediaPlayer.setVolume(1f, 1f);
                //        mediaPlayer.isLooping = true;
                //        mediaPlayer.prepare()
                //        mediaPlayer.start()
            }
        }
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy()
    }

    private fun startForegroundService() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                ""
            }

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val notification: Notification =
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("some title")
                .setContentText("some content")
                .setSmallIcon(R.drawable.ic_baseline_play_circle_filled_24)
                .setContentIntent(pendingIntent)
                .setTicker("ticker text")
                .build()

        startForeground(1, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String {
        val channel = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        channel.lightColor = Color.BLUE
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    private fun mediaPlayerFromAssets() {
        mediaPlayer = MediaPlayer().also {
            val descriptor: AssetFileDescriptor = assets.openFd("Vivaldi - Spring.mp3")
            it.setDataSource(
                descriptor.fileDescriptor,
                descriptor.startOffset,
                descriptor.length,
            )
            descriptor.close()
            it.prepare()
        }

    }


}