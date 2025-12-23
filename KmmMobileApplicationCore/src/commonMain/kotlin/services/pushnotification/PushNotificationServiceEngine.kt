package com.purenative.services.pushnotification

expect class PushNotificationServiceEngine() {
    var onNotificationDataReceived: ((Any) -> Unit)?
}