package com.purenative.system

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Timer(
    val interval: Long,
    val onTick: () -> Unit
) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    private var job: Job? = null

    fun resume() {
        if (job != null) return

        job = scope.launch {
            while (true) {
                delay(interval)
                onTick()
            }
        }
    }

    fun stop() {
        job?.cancel()
    }
}

fun createTimer(
    interval: Long,
    onTick: () -> Unit
): Timer {
    val timer = Timer(interval, onTick)
    timer.resume()
    return timer
}