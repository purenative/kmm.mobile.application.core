package com.purenative.system

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Debouncer(
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default),
    val interval: Long
) {
    private var job: Job? = null

    fun tryInvoke(
        onTry: () -> Unit,
        action: suspend () -> Unit
    ) {
        onTry()
        job?.cancel()
        job = scope.launch {
            delay(interval)
            action()
        }
    }
}