package screen

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

actual class ScreenActionInterceptorInvoker {
    private val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    actual fun invoke(action: suspend () -> Unit) {
        coroutineScope.launch { action() }
    }

    actual fun destroy() {
        coroutineScope.coroutineContext.cancelChildren()
    }
}