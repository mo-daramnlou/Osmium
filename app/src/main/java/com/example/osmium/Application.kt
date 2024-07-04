package com.example.osmium

import android.app.Application

class Application:Application() {

    companion object{
        lateinit var context: Application
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}