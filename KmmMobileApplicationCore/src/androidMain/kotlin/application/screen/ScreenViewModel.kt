package com.purenative.application.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class ScreenViewModel<ROUTE: ScreenRoute, STATE: ScreenState, INTERCEPTOR: ScreenActionInterceptor<ROUTE, STATE>>(
    val interceptor: INTERCEPTOR
): BaseScreenViewModel() {
    override val screenTitle: String? = interceptor.route.screenTitle

    var state by mutableStateOf<STATE>(interceptor.initialState)

    init {
        interceptor.onStateUpdated = {
            this.state = it
        }
    }

    override fun onCreate() {
        interceptor.onCreate()
    }
    override fun onAppear() {
        interceptor.onAppear()
    }
    override fun onDisappear() {
        interceptor.onDisappear()
    }
    override fun onDestroy() {
        interceptor.onDestroy()
    }
}