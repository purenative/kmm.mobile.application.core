package com.purenative.application.screen

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface ScreenStateObserver<STATE: ScreenState> {
    fun onNewState(newState: STATE)
}

class ScreenStateHolder<STATE: ScreenState>(
    var currentState: STATE,
    val observer: ScreenStateObserver<STATE>
) {
    private val updateMutex = Mutex()

    suspend fun updateState(onUpdateState: (STATE) -> STATE): Unit {
        updateMutex.withLock {
            val newState = onUpdateState(currentState)
            this.currentState = newState
            observer.onNewState(newState)
        }
    }

    suspend fun updateState(newState: STATE) {
        updateMutex.withLock {
            this.currentState = newState
            observer.onNewState(newState)
        }
    }
}