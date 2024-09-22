package com.dev.indrivertrigger

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Path
import android.graphics.Point
import android.os.Looper
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.dev.indrivertrigger.utils.ConstantValues
import com.dev.indrivertrigger.utils.PreferenceHelper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler


class MyAccessibilityService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Handle accessibility events if needed
        Log.d("ttttttt", "onAccessibilityEvent: ${event?.packageName}")
        if(event?.packageName!!.equals("sinet.startup.inDriver")){
//        if(event?.packageName!!.equals("com.skype.raider")){
            ConstantValues.IS_APP_OPENED = true
           /* val xPoint = PreferenceHelper.defaultPrefs(this).getInt("x_value", 0)
            val yPoint = PreferenceHelper.defaultPrefs(this).getInt("y_value", 0)
            val intentt = Intent("com.example.ACTION_TRIGGER_GESTURE")
            intentt.putExtra("x", xPoint)
            intentt.putExtra("y", yPoint)
            sendBroadcast(intentt)*/
        }else{
            ConstantValues.IS_APP_OPENED = false
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        Log.d("ttttttt", "onServiceConnected: Services connected")
        val filter = IntentFilter("com.example.ACTION_TRIGGER_GESTURE")
        registerReceiver(gestureReceiver, filter)
    }

    override fun onInterrupt() {
        // Handle interruptions or errors that occur during service execution
    }

    private val gestureReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
//            Toast.makeText(context,"Broadcast received", Toast.LENGTH_SHORT).show()
            Log.d("ttttttt", "onReceive: Received in Broadcast")
            val x = intent?.getIntExtra("x", 0)
            val y = intent?.getIntExtra("y", 0)
           val x2 = intent?.getIntExtra("x2", 0)
            val y2 = intent?.getIntExtra("y2", 0)
            if (x != null && y != null) {
//                Toast.makeText(context,"Tapping function", Toast.LENGTH_SHORT).show()

                simulateTap(context!!,x, y)
                Thread.sleep(1000)
//                expandNotificationBar()
                simulateTap(context!!,x2!!, y2!!)
            }

            // Keeping the main thread alive to let the coroutine execute
            Thread.sleep(2000)
        }
    }

    /**
     * Simulates a tap action at the specified coordinates.
     */
    fun simulateTap(context:Context,x: Int, y: Int) {
//        val path = Path()
//        path.moveTo(134.96704F, 1620.9229F)
        Log.d("ttttttt", "simulateTap: Tapping on Mail")
//        Toast.makeText(context,"Tapping start", Toast.LENGTH_SHORT).show()
        val clickPoint = Point(x, y) // Provide the coordinates where you want to tap

        val clickDuration = 50L // The duration of the tap, in milliseconds


        try {
            val builder = GestureDescription.Builder()
            builder.addStroke(GestureDescription.StrokeDescription(
                Path().apply { moveTo(clickPoint.x.toFloat(), clickPoint.y.toFloat()) },
                0L,
                clickDuration
            ))
            val gesture = builder.build()
            dispatchGesture(gesture, object : GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    Log.d("ttttttt", "Gesture dispatch completed successfully")
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    Log.d("ttttttt", "Gesture dispatch cancelled")
                }
            }, null)
        } catch (e: SecurityException) {
            // Handle security exceptions here
            Log.e("ttttttt", "Security exception: ${e.message}")
        } catch (e: Exception) {
            // Handle other exceptions here
            Log.e("ttttttt", "Exception: ${e.message}")
        }

    }


}
