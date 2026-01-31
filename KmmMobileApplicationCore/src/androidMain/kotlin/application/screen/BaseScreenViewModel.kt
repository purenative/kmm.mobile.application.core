package com.purenative.application.screen

import androidx.lifecycle.ViewModel

abstract class BaseScreenViewModel: ViewModel() {
    open val screenTitle: String? = null
    open val actions: List<ScreenTopAction> = emptyList()
    abstract fun onCreate()
    abstract fun onAppear()
    abstract fun onDisappear()
    abstract fun onDestroy()
}