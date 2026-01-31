package com.purenative.services.pushnotification

import com.purenative.system.OperatingSystem
import com.purenative.system.current
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

object PushNotificationService {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private var processor: PushNotificationProcessor? = null
    private var tokenHolder: PushNotificationServiceTokenHolder? = null

    var permissionAccess = PushNotificationServicePermissionAccess.UNDEFINED
        private set

    val engine: PushNotificationServiceEngine = {
        val engine = PushNotificationServiceEngine()
        engine.onNotificationDataReceived = ::handlePushNotificationData
        engine
    }()

    fun setup(
        processor: PushNotificationProcessor,
        tokenProvider: PushNotificationServiceTokenProvider,
        tokenUpdateProcessor: PushNotificationTokenUpdateProcessor
    ) {
        this.processor = processor
        this.tokenHolder = PushNotificationServiceTokenHolder(tokenProvider, tokenUpdateProcessor)

        when (OperatingSystem.current()) {
            OperatingSystem.IOS -> updatePermissionAccessIfNeeded()
            OperatingSystem.ANDROID -> retrieveDeviceToken()
        }
    }

    suspend fun tokens(): PushNotificationServiceTokens =
        tokenHolder?.tokens() ?: PushNotificationServiceTokens(null, null)

    fun retrieveDeviceToken() =
        tokenHolder?.retrieveDeviceToken() ?: Unit

    private fun handlePushNotificationData(notificationData: Any) {
        if (OperatingSystem.current() == OperatingSystem.ANDROID) {
            updatePermissionAccessIfNeeded()
        }

        processor?.let { processor ->
            coroutineScope.launch {
                processor.processPushNotification(notificationData)
            }
        }
    }

    private fun updatePermissionAccessIfNeeded() {
        if (permissionAccess != PushNotificationServicePermissionAccess.UNDEFINED) return

        coroutineScope.launch {
            permissionAccess =
                if (requestPlatformSpecificPushNotificationPermission()) PushNotificationServicePermissionAccess.GRANTED else PushNotificationServicePermissionAccess.DENIED
        }
    }
}

expect suspend fun requestPlatformSpecificPushNotificationPermission(): Boolean