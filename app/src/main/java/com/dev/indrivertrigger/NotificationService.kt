package com.dev.indrivertrigger

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dev.indrivertrigger.utils.ConstantValues
import com.dev.indrivertrigger.utils.PreferenceHelper
import java.io.ByteArrayOutputStream

class NotificationService : NotificationListenerService() {
    var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("NotificationListener", "onListenerConnected: ")
    }

    override fun getActiveNotifications(): Array<StatusBarNotification> {
        return super.getActiveNotifications()
        Log.d("NotificationListener", "getActiveNotifications: ")

    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        Log.d("NotificationListener", "onListenerDisconnected: ")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        val pack = sbn.packageName
        if(pack.contains("sinet.startup.inDriver")){
            Log.d("TAG", "onNotificationPosted: Yes this is indrive notification")
//            Toast.makeText(context,"Yes this is indrive notification", Toast.LENGTH_SHORT).show()

            val xPoint = PreferenceHelper.defaultPrefs(this).getInt("x_value", 0)
            val yPoint = PreferenceHelper.defaultPrefs(this).getInt("y_value", 0)
            val x2Point = PreferenceHelper.defaultPrefs(this).getInt("x2_value", 0)
            val y2Point = PreferenceHelper.defaultPrefs(this).getInt("y2_value", 0)

            //Check if the desired app is open or not. If Open then send the broadcast
            if(ConstantValues.IS_APP_OPENED){
//                Toast.makeText(context,"App is open", Toast.LENGTH_SHORT).show()
                val intentt = Intent("com.example.ACTION_TRIGGER_GESTURE")
                intentt.putExtra("x", xPoint)
                intentt.putExtra("y", yPoint)
                intentt.putExtra("x2", x2Point)
                intentt.putExtra("y2", y2Point)
                sendBroadcast(intentt)

            }else{
                Log.d("TAG", "onNotificationPosted: Indriver App is not open")
//                Toast.makeText(context,"Indriver App is not open", Toast.LENGTH_SHORT).show()
            }

        }else{
            Log.d("TAG", "onNotificationPosted: Not indriver notification")
//            Toast.makeText(context,"Not indriver notification", Toast.LENGTH_SHORT).show()
        }
        var ticker = ""
        if (sbn.notification.tickerText != null) {
            ticker = sbn.notification.tickerText.toString()
        }
        val extras = sbn.notification.extras
        val title = extras.getString("android.title").toString()
        val text = extras.getCharSequence("android.text").toString()
        val id1 = extras.getInt(Notification.EXTRA_SMALL_ICON)
        val id = sbn.notification.largeIcon
        Log.i("Package", pack)
        Log.i("Ticker", ticker)
        Log.i("Title", title!!)
        Log.i("Text", text)
        val msgrcv = Intent("Msg")
        msgrcv.putExtra("package", pack)
        msgrcv.putExtra("ticker", ticker)
        msgrcv.putExtra("title", title)
        msgrcv.putExtra("text", text)
        if (id != null) {
            val stream = ByteArrayOutputStream()
            id.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            msgrcv.putExtra("icon", byteArray)
        }
        LocalBroadcastManager.getInstance(context!!).sendBroadcast(msgrcv)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.i("Msg", "Notification Removed")
    }



}