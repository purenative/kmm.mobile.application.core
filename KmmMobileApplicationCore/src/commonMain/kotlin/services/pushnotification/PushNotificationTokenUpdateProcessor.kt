package com.purenative.services.pushnotification

interface PushNotificationTokenUpdateProcessor {
    suspend fun processTokens(tokens: PushNotificationServiceTokens): Boolean
}