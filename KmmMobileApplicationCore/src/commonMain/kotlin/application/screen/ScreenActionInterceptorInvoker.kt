package com.purenative.application.screen

internal expect class ScreenActionInterceptorInvoker() {
    fun invoke(action: suspend () -> Unit)
    fun destroy()
}