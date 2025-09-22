package screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch

actual class ScreenActionInterceptorInvoker {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    actual fun invoke(action: suspend () -> Unit) {
        coroutineScope.launch { action() }
    }

    actual fun destroy() {
        coroutineScope.coroutineContext.cancelChildren()
    }
}