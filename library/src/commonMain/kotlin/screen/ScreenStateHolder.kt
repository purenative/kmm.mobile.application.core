package screen

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// Контейнер для хранения и синхронизации State экрана приложения
class ScreenStateHolder<STATE: ScreenState>(
    var currentState: STATE
) {
    private val updateMutex = Mutex()

    var onStateUpdated: ((STATE) -> Unit)? = null

    suspend fun updateState(onUpdateState: (STATE) -> STATE): Unit {
        updateMutex.withLock {
            val newState = onUpdateState(currentState)
            this.currentState = newState
            onStateUpdated?.let { it(newState) }
        }
    }

    suspend fun updateState(newState: STATE) {
        updateMutex.withLock {
            this.currentState = newState
            onStateUpdated?.let { it(newState) }
        }
    }
}