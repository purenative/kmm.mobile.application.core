package com.purenative.services.pushnotification

interface PushNotificationProcessor {
    suspend fun processPushNotification(notificationData: Any)
}