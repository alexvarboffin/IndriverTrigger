package com.dev.indrivertrigger

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout

class TouchService : Service() {
    private var touchLayout: LinearLayout? = null
    private var mWindowManager: WindowManager? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // Create a layout for touch events
        touchLayout = LinearLayout(this)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        touchLayout!!.layoutParams = lp
        touchLayout!!.setBackgroundColor(Color.TRANSPARENT) // Make it transparent
        touchLayout!!.setOnTouchListener { view, motionEvent ->
            Log.i(
                "ttttttt",
                "Action: " + motionEvent.action + " X: " + motionEvent.rawX + " Y: " + motionEvent.rawY
            )
            true // Return true to consume the event
        }

        // Obtain the WindowManager
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Define layout parameters for the touch layout
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Use TYPE_APPLICATION_OVERLAY for Android 8.0 and above
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Don't let the touch events outside this window to be consumed by other windows
                PixelFormat.TRANSLUCENT
            )
        } else {
            WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,  // Use TYPE_APPLICATION_OVERLAY for Android 8.0 and above
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,  // Don't let the touch events outside this window to be consumed by other windows
                PixelFormat.TRANSLUCENT
            )
        }

        // Add the touch layout to the WindowManager
        mWindowManager!!.addView(touchLayout, params)

        startService(Intent(this, BackgroundService::class.java))
    }


    override fun onDestroy() {
        super.onDestroy()
        // Remove the touch layout when the service is destroyed
        if (mWindowManager != null && touchLayout != null) {
            mWindowManager!!.removeView(touchLayout)
        }
    }

    companion object {
        private const val TAG = "TouchService"
    }
}
