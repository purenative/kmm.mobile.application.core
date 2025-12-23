package com.purenative.services.pushnotification

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import com.purenative.services.permission.PermissionService

actual class PushNotificationServiceEngine {
    actual var onNotificationDataReceived: ((Any) -> Unit)? = null

    internal fun handleNewIntent(intent: Intent) {
        val bundle = intent.extras ?: Bundle()
        onNotificationDataReceived?.invoke(bundle)
    }
}

actual suspend fun requestPlatformSpecificPushNotificationPermission(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
        PermissionService.requestPermission(Manifest.permission.POST_NOTIFICATIONS)
    else true