package com.dev.indrivertrigger

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.dev.indrivertrigger.ui.authentication.LogInActivity
import com.dev.indrivertrigger.utils.AccessibilityUtils
import com.dev.indrivertrigger.utils.PreferenceHelper
import com.dev.indrivertrigger.utils.PreferenceHelper.clearPreferences
import com.dev.indrivertrigger.utils.PreferenceHelper.set


class MainActivity2 : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val xEt = findViewById<EditText>(R.id.xET)
        val yEt = findViewById<EditText>(R.id.yET)
        val main = findViewById<LinearLayout>(R.id.main)
        val xEt2 = findViewById<EditText>(R.id.xET2)
        val yEt2 = findViewById<EditText>(R.id.yET2)
        val submitBtn = findViewById<Button>(R.id.submitBtn)
        val logoutIcon = findViewById<ImageView>(R.id.logout)

        /*main.setOnTouchListener{_, event ->
            val x = event.x.toInt()
            val y = event.y.toInt()

            Log.d("coordinates", "X: $x  Y: $y")
            // Check if the touch coordinates are within a specific range
            false
        }*/
        submitBtn.setOnClickListener {
            try {
                val xPoint = Integer.parseInt(xEt.text.toString())
                val yPoint = Integer.parseInt(yEt.text.toString())

                val x2Point = Integer.parseInt(xEt2.text.toString())
                val y2Point = Integer.parseInt(yEt2.text.toString())

                Toast.makeText(this, "Tapping the co ordinates in 5 secs", Toast.LENGTH_SHORT).show()

                PreferenceHelper.defaultPrefs(this)["x_value"] = xPoint
                PreferenceHelper.defaultPrefs(this)["y_value"] = yPoint

                //Save 2nd coordinates
                PreferenceHelper.defaultPrefs(this)["x2_value"] = x2Point
                PreferenceHelper.defaultPrefs(this)["y2_value"] = y2Point
//                startService(Intent(this, NotificationService::class.java))

                xEt.setText("")
                yEt.setText("")
                xEt2.setText("")
                yEt2.setText("")
            }catch (e:Exception){
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        logoutIcon.setOnClickListener {
            PreferenceHelper.defaultPrefs(this).clearPreferences(this)
            finishAffinity()
            startActivity(Intent(this@MainActivity2, LogInActivity::class.java))
        }
    }


    override fun onResume() {
        super.onResume()
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivity(intent)
        }else if(!AccessibilityUtils.isAccessibilityServiceEnabled(this, MyAccessibilityService::class.java.name)){
            AccessibilityUtils.openAccessibilitySettings(this)
        }else if (!isNotificationServiceEnabled()){
            requestNotificationListenerPermission()
        } else if (ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.EXPAND_STATUS_BAR
        ) != PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.EXPAND_STATUS_BAR), 1);
        }
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val packageName = packageName
        val flat = Settings.Secure.getString(contentResolver, "enabled_notification_listeners")
        return flat != null && flat.contains(packageName)
    }

    private fun requestNotificationListenerPermission() {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }



}