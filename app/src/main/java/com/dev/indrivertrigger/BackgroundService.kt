package com.dev.indrivertrigger

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log

class BackgroundService : Service() {

    private val receiver = MyBroadcastReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val x = intent?.getIntExtra("x", 0)
        val y = intent?.getIntExtra("y", 0)
        Log.d("ttttttt", "onStartCommand: Calling broadcast")
        val intentt = Intent("com.example.ACTION_TRIGGER_GESTURE")
        intentt.putExtra("x", x)
        intentt.putExtra("y", y)
        sendBroadcast(intentt)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver
        unregisterReceiver(receiver)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}