package com.purenative.application

import android.content.Context

object ApplicationContextHolder {
    lateinit var applicationContext: Context
        private set

    fun init(context: Context) {
        applicationContext = context.applicationContext
    }
}