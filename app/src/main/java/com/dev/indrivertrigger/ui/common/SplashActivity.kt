package com.dev.indrivertrigger.ui.common

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dev.indrivertrigger.MainActivity2
import com.dev.indrivertrigger.R
import com.dev.indrivertrigger.ui.authentication.LogInActivity
import com.dev.indrivertrigger.utils.ConstantValues.TOKEN
import com.dev.indrivertrigger.utils.PreferenceHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        token = PreferenceHelper.defaultPrefs(this).getString(TOKEN, "") ?: ""

        initSplash()

    }


    private fun initSplash(){
        lifecycleScope.launch {
            delay(3000)
            if(token.isNotEmpty()){
                startActivity(Intent(this@SplashActivity, MainActivity2::class.java))
            }else{
                startActivity(Intent(this@SplashActivity, LogInActivity::class.java))
            }
            finish()
        }
    }
}