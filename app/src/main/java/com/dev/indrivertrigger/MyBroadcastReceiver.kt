package com.dev.indrivertrigger

import android.app.Instrumentation
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyBroadcastReceiver : BroadcastReceiver() {
    var m_Instrumentation: Instrumentation? = null

    @OptIn(DelicateCoroutinesApi::class)
    override fun onReceive(p0: Context?, p1: Intent?) {
        m_Instrumentation = Instrumentation()

        val x = 0 //your x coord in screen.

        val y = 0 // your y coord in screen.

        CoroutineScope(Dispatchers.IO).launch {
            m_Instrumentation!!.sendPointerSync(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x.toFloat(), y.toFloat(), 0
                )
            )
            m_Instrumentation!!.sendPointerSync(
                MotionEvent.obtain(
                    SystemClock.uptimeMillis(),
                    SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x.toFloat(), y.toFloat(), 0
                )
            )
        }
        showNotification(p0!!,"test","test message")
//         clickOnCoordinates(934,1896)
          Toast.makeText(p0, "Broadcast Received" , Toast.LENGTH_LONG).show()
    }

    // Function to show a local notification
    fun showNotification(context: Context, title: String, message: String) {
        // Create a NotificationManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel for devices running Android Oreo (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "my_channel_id"
            val channelName = "My Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(channel)
        }

        // Create a notification builder
        val builder = NotificationCompat.Builder(context, "my_channel_id")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // Show the notification
        notificationManager.notify(0, builder.build())
    }

    private fun clickOnCoordinates(x: Int, y: Int) {
        val downTime = SystemClock.uptimeMillis()
        val eventTime = SystemClock.uptimeMillis()
        val instrumentation = Instrumentation()

        GlobalScope.launch(Dispatchers.IO) {
            // Simulate touch down event
            val downEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_DOWN,
                x.toFloat(),
                y.toFloat(),
                0
            )
            instrumentation.sendPointerSync(downEvent)

            // Simulate touch up event
            val upEvent = MotionEvent.obtain(
                downTime + 100,
                eventTime + 100,
                MotionEvent.ACTION_UP,
                x.toFloat(),
                y.toFloat(),
                0
            )
            instrumentation.sendPointerSync(upEvent)
        }



    }
}