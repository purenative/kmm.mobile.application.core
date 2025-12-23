package com.purenative.services.pushnotification

import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionBadge
import platform.UserNotifications.UNNotificationPresentationOptionBanner
import platform.UserNotifications.UNNotificationPresentationOptionSound
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.darwin.NSObject
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

actual class PushNotificationServiceEngine: NSObject, UNUserNotificationCenterDelegateProtocol {
    actual var onNotificationDataReceived: ((Any) -> Unit)? = null

    actual constructor() {
        UNUserNotificationCenter.currentNotificationCenter().setDelegate(this)
        registerForRemoteNotifications()
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        didReceiveNotificationResponse: UNNotificationResponse,
        withCompletionHandler: () -> Unit
    ) {
        val userInfo = didReceiveNotificationResponse.notification.request.content.userInfo
        onNotificationDataReceived?.invoke(userInfo)
    }

    override fun userNotificationCenter(
        center: UNUserNotificationCenter,
        willPresentNotification: UNNotification,
        withCompletionHandler: (UNNotificationPresentationOptions) -> Unit
    ) {
        val options = UNNotificationPresentationOptionBadge or
                UNNotificationPresentationOptionBanner or
                UNNotificationPresentationOptionSound
        withCompletionHandler(options)
    }

    private fun registerForRemoteNotifications() {
        dispatch_async(dispatch_get_main_queue()) {
            UIApplication.sharedApplication.registerForRemoteNotifications()
        }
    }
}

actual suspend fun requestPlatformSpecificPushNotificationPermission(): Boolean =
    suspendCoroutine { continuation ->
        requestIOSPushNotificationPermission {
            continuation.resume(it)
        }
    }

private fun requestIOSPushNotificationPermission(completionHandler: (Boolean) -> Unit) {
    dispatch_async(dispatch_get_main_queue()) {
        val options = UNAuthorizationOptionAlert or
                UNAuthorizationOptionBadge or
                UNAuthorizationOptionSound
        UNUserNotificationCenter.currentNotificationCenter().requestAuthorizationWithOptions(
            options,
            { accessGranted, _ -> completionHandler(accessGranted) }
        )
    }
}