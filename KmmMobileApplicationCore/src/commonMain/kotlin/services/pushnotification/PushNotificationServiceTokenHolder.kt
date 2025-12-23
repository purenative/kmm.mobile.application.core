package com.purenative.services.pushnotification

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

internal class PushNotificationServiceTokenHolder(
    val tokenProvider: PushNotificationServiceTokenProvider,
    val tokenUpdateProcessor: PushNotificationTokenUpdateProcessor
) {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val settings = Settings()
    private val tokenMutex = Mutex()

    init {
        tokenProvider.onTokenUpdated = { newToken ->
            coroutineScope.launch {
                onTokenUpdated(newToken)
            }
        }
    }

    suspend fun tokens(): PushNotificationServiceTokens = tokenMutex.withLock {
        PushNotificationServiceTokens(
            settings.getStringOrNull(PUSH_NOTIFICATION_TOKEN_PREVIOUS),
            settings.getStringOrNull(PUSH_NOTIFICATION_TOKEN_CURRENT)
        )
    }

    fun retrieveDeviceToken() {
        tokenProvider.retrieveDeviceToken()
    }

    private suspend fun onTokenUpdated(newToken: String?) {
        tokenMutex.withLock {
            val newToken = newToken ?: return
            val currentToken = settings.getString(PUSH_NOTIFICATION_TOKEN_CURRENT, "")

            if (newToken == currentToken) return

            val tokens = PushNotificationServiceTokens(
                currentToken,
                newToken
            )
            val applied = tokenUpdateProcessor.processTokens(tokens)

            if (!applied) return
            settings.putString(PUSH_NOTIFICATION_TOKEN_PREVIOUS, tokens.previous.orEmpty())
            settings.putString(PUSH_NOTIFICATION_TOKEN_CURRENT, tokens.current.orEmpty())
        }
    }

    companion object {
        private const val PUSH_NOTIFICATION_TOKEN_PREVIOUS = "push_notification_token_previous"
        private const val PUSH_NOTIFICATION_TOKEN_CURRENT = "push_notification_token_current"
    }
}