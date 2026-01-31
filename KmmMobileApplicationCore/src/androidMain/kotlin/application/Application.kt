package com.purenative.application

import android.app.Application

open class Application: Application() {
    override fun onCreate() {
        super.onCreate()

        ApplicationContextHolder.init(this)
        onStartApplication()
    }

    open fun onStartApplication() { }
}