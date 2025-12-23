package com.purenative.application.screen

import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@OptIn(ExperimentalObjCName::class)
@ObjCName("KMMScreenActionInterceptor", "ScreenActionInterceptor", true)
open class ScreenActionInterceptor<ROUTE: ScreenRoute, STATE: ScreenState>(
    val route: ROUTE,
    val initialState: STATE
): ScreenStateObserver<STATE> {
    private val invoker = ScreenActionInterceptorInvoker()
    private val stateHolder = ScreenStateHolder<STATE>(initialState, this)

    var isAppeared: Boolean = false
        private set

    val currentState: STATE
        get() = stateHolder.currentState
    var onStateUpdated: ((STATE) -> Unit)? = null

    open fun onCreate() { }
    open fun onAppear() {
        isAppeared = true
    }
    open fun onDisappear() {
        isAppeared = false
    }
    fun onDestroy() {
        invoker.destroy()
    }

    fun invoke(action: suspend (stateHolder: ScreenStateHolder<STATE>) -> Unit) {
        invoker.invoke {
            action(stateHolder)
        }
    }

    override fun onNewState(newState: STATE) {
        onStateUpdated?.let { it(newState) }
    }
}