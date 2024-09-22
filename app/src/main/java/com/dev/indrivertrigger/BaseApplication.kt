package com.dev.indrivertrigger


import android.app.Application
import android.content.Context

class Myapp :Application() {

    companion object{
        lateinit var appContext: Context
        var myApp: Myapp? = null
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        myApp = this
    }
}