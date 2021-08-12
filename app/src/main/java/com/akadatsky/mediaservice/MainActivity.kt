package com.akadatsky.mediaservice

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun start(view: View) {
        val intent = Intent(MEDIA_ACTION)
        intent.putExtra(PLAY, true)
        sendBroadcast(intent)
    }

    fun stop(view: View) {
        val intent = Intent(MEDIA_ACTION)
        intent.putExtra(PLAY, false)
        sendBroadcast(intent)
    }

    fun startService(view: View) {
        val intent = Intent(this, MyService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    fun stopService(view: View) {
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
    }
}